package sooyu.webview.webviewjavascriptbridge.impl;

import sooyu.webview.BaseWebviewActivity;
import sooyu.webview.Utils.L;
import sooyu.webview.Utils.StringUtil;
import sooyu.webview.Utils.WebUrlUtils;
import sooyu.webview.webviewjavascriptbridge.WVJBWebViewClient;

/**
 * H5与Native的Jump交互方法
 * @author LangShaoPeng
 *
 */
public class WVJBHandlerJumpForJSBrige implements WVJBWebViewClient.WVJBHandler {
	/** 上下文 */
	private BaseWebviewActivity mActivity=null;

	/** 调用协议  - JSON解析key */
	public static final String H5_CALL_JSON_COMMAND_KEY ="param" ;
	/** 调用协议 scheme 头 */
	public static final String H5_CALL_SCHEME ="yintaimobile" ;
	/** H5调用方法名 - 跳转到登录页*/
	public static final String H5_CALL_METHOD_LOGIN ="Login" ;
	/** H5调用方法名 - 关闭当前页*/
	public static final String H5_CALL_METHOD_CLOSECURRENTPAGE ="CloseCurrentPage" ;
	/** H5调用方法名 - 获取用户信息*/
	public static final String H5_CALL_METHOD_GETUERSINFO ="GetUersInfo" ;
	/** H5调用方法名 - 方法检查 */
	public static final String H5_CALL_METHOD_CHECKMETHOD ="CheckMethod" ;

	public WVJBHandlerJumpForJSBrige(BaseWebviewActivity baseActivity){
		this.mActivity=baseActivity;
	}

	@Override
	public void request(Object data, WVJBWebViewClient.WVJBResponseCallback callback) {
		if(data!=null){
			L.d("WVJB", data.toString());
			String jsonStr = data.toString();
			String commond = WebUrlUtils.getJsonValue(jsonStr,H5_CALL_JSON_COMMAND_KEY);
			if (StringUtil.isNotEmpty(commond)) {
				String scheme = WebUrlUtils.getScheme(commond);
				if(H5_CALL_SCHEME.equalsIgnoreCase(scheme)) {
				}
					/*{    //根据scheme判断是否为对应调用方法
					String callMethod = WebUrlUtils.getCallCommand(commond);
					if (StringUtil.isNotEmpty(callMethod)) {   //根据 callMethod 调用对应方法
						if(callMethod.contains(H5_CALL_METHOD_LOGIN)){
							//DispatchHelper.startLogin(mActivity);
							callback.callback("call method Login!");
						}else if (callMethod.contains(H5_CALL_METHOD_CLOSECURRENTPAGE)) {
							callback.callback("call method CloseCurrentPage!");
							if (null != mActivity && !mActivity.isFinishing()) {
								//mActivity.finish();
							}
						} else if (callMethod.contains(H5_CALL_METHOD_GETUERSINFO)) {
							callback.callback(H5CallNativeUtils.getuserinfo(mActivity));
						} else if (callMethod.contains(H5_CALL_METHOD_CHECKMETHOD)) {
							callback.callback(true);
						}else{
							callback.callback("no match method!");
						}
					}else{
						callback.callback("method is null!");
					}
				}else{
					callback.callback("scheme mismatching!");
				}*/
				}
			} else {
				callback.callback("no command!");
			}
		}
	}