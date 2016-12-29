package sooyu.webview.webviewjavascriptbridge.impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import sooyu.webview.Utils.GsonUtil;
import sooyu.webview.Utils.JSBrigeWebViewClient;
import sooyu.webview.Utils.L;
import sooyu.webview.Utils.StringUtil;
import sooyu.webview.oauth.OAuthTokenResponse;
import sooyu.webview.oauth.OAuthUtils;
import sooyu.webview.oauth.Token;
import sooyu.webview.oauth.TokenH5;
import sooyu.webview.webviewjavascriptbridge.WVJBWebViewClient;

/**
 * H5获取Native保存的AccessToken的交互方法
 * @author LangShaoPeng
 *
 */
public class WVJBHandlerAppToken implements WVJBWebViewClient.WVJBHandler {
	//异步获取apptoken后，通过该方法返回结果
	private static final String METHOD_RESP_GET_VALIDAPPTOKEN = "getValidAppTokenByAsyncSuccess";
	public enum AppTokenType{
		Normal,   //获取普通Token
		ValidAppTokenBySync,  //检测是否过期，如果过期需要再次获取并同步返回
		ValidAppTokenByAsync  //检测是否过期，获取完之后通过H5的getValidAppTokenByAsyncSuccess通知结果
	}

	private AppTokenType tokenType;
	private JSBrigeWebViewClient webViewClient ;
	private Handler respHandler = null ;
	public WVJBHandlerAppToken(JSBrigeWebViewClient webViewClient , AppTokenType tokenType){
		this.tokenType = tokenType;
		this.webViewClient =webViewClient ;
	}
	@Override
	public void request(Object data, WVJBWebViewClient.WVJBResponseCallback callback) {
		try {
			if( tokenType == AppTokenType.Normal){
				//将tokenH5序列化成JSON格式的字符串传给H5
				responseToken(callback,getAppToken());
			}else if(tokenType == AppTokenType.ValidAppTokenBySync){
				//同步返回数据
				checkTokenAndExec(callback);
			}else if(tokenType == AppTokenType.ValidAppTokenByAsync){
				//异步返回数据
				callback.callback("success");
				checkTokenAndExec(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			callback.callback("");
		}
	}

	private Handler getRespHandler(){
		if( respHandler == null ){
			respHandler = new Handler(Looper.getMainLooper()){
				@Override
				public void handleMessage(Message msg) {
					if( msg != null && msg.obj instanceof TokenObject){
						TokenObject object = (TokenObject)msg.obj;
						responseToken(object.callback,object.tokenH5);
					}
				}
			};
		}
		return respHandler;
	}

	/**
	 * 通过消息返回数据
	 * @param callback
	 * @param tokenH5
     */
	private void responseTokenByMessage(WVJBWebViewClient.WVJBResponseCallback callback,
							 TokenH5 tokenH5){
		TokenObject object = new TokenObject();
		object.callback = callback;
		object.tokenH5 = tokenH5;
		Message msg = Message.obtain();
		msg.obj = object;
		getRespHandler().sendMessage(msg);
	}

	/**
	 * 检查是否过期，如果过期重新获取
	 */
	private void checkTokenAndExec(final WVJBWebViewClient.WVJBResponseCallback callback){
		if( OAuthUtils.localHaveAvailableToken()){
			//Token未过期，直接返回
			responseToken(callback,getAppToken());
		}else {

			//Token快过期或者已过期，重新请求
			new Thread(new Runnable() {
				@Override
				public void run() {
					OAuthTokenResponse response = OAuthUtils.getAccessToken();
					TokenH5 tokenH5 = getAppToken();
					if(!(response instanceof Token)){
						//获取数据失败，清空token信息
						tokenH5.setAccess_token("");
					}
					responseTokenByMessage(callback,tokenH5);
				}
			}).start();
		}
	}

	/**
	 * 给H5反馈AppToken信息
	 * @param callback
	 * 		回调对象，当不为null时通过callback对象返回数据，否则通过H5的js方法返回数据
	 * @param appToken
     */
	private void responseToken(final WVJBWebViewClient.WVJBResponseCallback callback,
							   TokenH5 appToken) {
		if( appToken == null ){
			return ;
		}
		String json = "";
		if(!StringUtil.isEmpty(appToken.getAccess_token())){
			json = GsonUtil.toJson(appToken);
		}
		if (callback != null) {
			callback.callback(json);
		} else if (webViewClient != null) {
			webViewClient.callHandler(METHOD_RESP_GET_VALIDAPPTOKEN,
					json,
					new WVJBWebViewClient.WVJBResponseCallback() {
						@Override
						public void callback(Object data) {
							L.d("WVJB", data.toString());
						}
					});
		}
	}

	/**
	 * 获取App Token
	 * @return
     */
	private TokenH5 getAppToken(){
		TokenH5 tokenH5=new TokenH5();
		// Token值
		tokenH5.setAccess_token(OAuthUtils.getLocalSaveToken());
		// Token类型
		//tokenH5.setToken_type(GatewayUtil.getTokenType(OAuthUtils.getLocalSaveToken()));
		// Token生命周期
		tokenH5.setExpires_in(OAuthUtils.getLocalSaveTokenExpiresIn());
		// 获取Token的时间
		tokenH5.setGettoken_time(OAuthUtils.getLocalSaveTokenTime());
		return tokenH5;
	}

	class TokenObject{
		public WVJBWebViewClient.WVJBResponseCallback callback ;
		public TokenH5 tokenH5;
	}
}
