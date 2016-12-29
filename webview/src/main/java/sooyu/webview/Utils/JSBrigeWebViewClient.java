package sooyu.webview.Utils;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import sooyu.webview.BaseWebviewActivity;
import sooyu.webview.config.H5CallNativeConstant;
import sooyu.webview.webviewjavascriptbridge.WVJBWebViewClient;
import sooyu.webview.webviewjavascriptbridge.impl.WVJBHandlerAppToken;
import sooyu.webview.webviewjavascriptbridge.impl.WVJBHandlerJumpForJSBrige;

/**为了实现集成JSWebViewBridge而写*/
public class JSBrigeWebViewClient extends WVJBWebViewClient {
	/** 上下文 */
	private BaseWebviewActivity mActivity=null;
	
	public JSBrigeWebViewClient(WebView webView, final BaseWebviewActivity mActivity) {
		// support js send
		super(webView, new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object data, WVJBResponseCallback callback) {
			}
		});
		this.mActivity=mActivity;
		//通过JSBrige注册交互方法
		//互调的实现
		registerHandler("jumpForJSBrige", new WVJBHandlerJumpForJSBrige(mActivity));
		registGetAppTokenHandlers();
	}

	/**
	 * 注册获取App Token的相关handler
	 */
	private void registGetAppTokenHandlers(){
		//H5获取Nativie保存的OAuth AccessToken，包括AccessToken值、获取AccessToken的时间、AccessToken的有效周期
		registerHandler("getAppToken", new WVJBHandlerAppToken(null, WVJBHandlerAppToken.AppTokenType.Normal));
		//同步获取AppToken，当AppToken快过期时，需要重新取Token，并同步返回数据
		registerHandler("getValidAppTokenBySync", new WVJBHandlerAppToken(this,WVJBHandlerAppToken.AppTokenType.ValidAppTokenBySync));
		//异步获取AppToken，获取完之后通过H5的getValidAppTokenByAsyncSuccess通知结果
		registerHandler("getValidAppTokenByAsync", new WVJBHandlerAppToken(this,WVJBHandlerAppToken.AppTokenType.ValidAppTokenByAsync));
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		sendEmptyMessage(H5CallNativeConstant.H5CALLNATIVE_SHOWPROGRESSDLG_MSGID);//显示LOADING框
		sendEmptyMessage(H5CallNativeConstant.H5CALLNATIVE_LOAD_START_MSGID);//显示LOADING框
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		sendEmptyMessage(H5CallNativeConstant.H5CALLNATIVE_DISSPROGRESSDLG_MSGID);//关闭LOADING框
		super.onPageFinished(view, url);
	}
	
	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		view.loadData("","text/html", "UTF-8");//错误处理
	}

	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		/*if(ExportPackageUtils.isDevMode ){*/
			//非线上环境允许页面中的https请求(证书不正确)
			handler.proceed();
	/*	}else {
			//线上环境当发现https的证书不正确时,cancel调
			super.onReceivedSslError(view, handler, error);
		}*/
	}

	/**
	 * view 资源本地拦截  暂时屏蔽
	 * @param view
	 * @param url
	 * @return
     */
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		try{
			/*if(H5HttpHijackUtils.isAllowInterceptUrl()){
				WebResourceResponse hijackResponse = H5HttpHijackUtils.checkHijackUrl(mActivity,url);
				if(hijackResponse!=null){
					return hijackResponse;
				}
			}
			//拦截请求URL,替换本地资源
			WebResourceResponse response=H5ResourceRequestIntercept.
					interceptResourceRequest2LocalResource(url, mActivity.getApplicationContext());
			if(null!=response){
				return response;
			}*/
		}catch (Exception e){

		}

		return super.shouldInterceptRequest(view, url);
	}
	
	/**
	 * 发送消息
	 * @param what
	 */
	private void sendEmptyMessage(int what){
		if(null!=mActivity&&!mActivity.isFinishing()){
			mActivity.basicHandler.sendEmptyMessage(what);
		}
	}
}
