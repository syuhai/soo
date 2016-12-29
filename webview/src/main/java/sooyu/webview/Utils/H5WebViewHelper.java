package sooyu.webview.Utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import sooyu.webview.BaseWebviewActivity;


/**
 * 	WebView的工具类
 */
public class H5WebViewHelper {
	public final String TAG = getClass().getSimpleName();
	private final static String ENCODE_TYPE = "UTF_8";//"gb2312","UTF_8",Encoding.UTF_8.name()
	private final static String MIME_TYPE  = "text/html; charset=UTF-8";
	
	
	/**
	 * 初始化WebView，包括常用的WebView相关设置，<br>
	 * 但不包括addJavascriptInterface()、setWebChromeClient()等方法实现
	 * @param webView
	 * @return
	 */
	public static WebView initWebView(WebView webView) {
		if (webView==null) {
			return webView;
		}
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 去掉滚动条占位
		webView.requestFocus();// 支持网页内部操作，比如点击按钮
		initWebSettings(webView);
		return webView;
	}

	/** 设置UserAgentString */
	public static void setUserAgentString(WebView webView, String appInfo) {
		if(webView!=null&&!StringUtil.isEmpty(appInfo)){
			WebSettings webSettings = webView.getSettings();
			String userAgentString = webSettings.getUserAgentString();
			webSettings.setUserAgentString(userAgentString+appInfo);
		}
	}
	/** 初始化WebView */
	private static WebView initWebSettings(WebView webView) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);// WebView启用Javascript脚本执行
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// ws.setBlockNetworkImage(true);//把图片加载放在最后来加载渲染
		webSettings.setRenderPriority(RenderPriority.HIGH);// 提高渲染的优先级
		webSettings.setDomStorageEnabled(true);// 设置可以使用localStorage
		
		webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
		webSettings.setBuiltInZoomControls(true);// 设置是否启用内置的缩放控件
		webSettings.setSupportZoom(false);// 设置是否支持缩放
		webSettings.setDisplayZoomControls(false);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setDatabaseEnabled(true);
		
		//H5页面视频播放相关
		webSettings.setPluginState(PluginState.ON);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		//webSettings.setAllowFileAccess(true);// 若html是一个文件框的话,就可以浏览本地文件
//		  int screenDensity = getResources().getDisplayMetrics().densityDpi;
//		  WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
//		  switch (screenDensity) { 
//		  case DisplayMetrics.DENSITY_LOW: 
//			  zoomDensity = WebSettings.ZoomDensity.CLOSE; 
//			  break; 
//		  case DisplayMetrics.DENSITY_MEDIUM: 
//			  zoomDensity =	 WebSettings.ZoomDensity.MEDIUM; 
//			  break; 
//		  default:
//			  zoomDensity =	 WebSettings.ZoomDensity.FAR; 
//			  break; 
//		  }
//		  webSettings.setDefaultZoom(zoomDensity);
		return webView;
	}
	
	/**
	 * 停止播放
	 * @param webView
	 */
	public static void setWebViewResume(WebView webView){
		try {
			if(null!=webView){
				webView.getClass().getMethod("onResume").invoke(webView,(Object[])null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 继续播放
	 * @param webView
	 */
	public static void setWebViewPause(WebView webView){
		try {
			if(null!=webView){
				webView.getClass().getMethod("onPause").invoke(webView,(Object[])null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 删除所有cookie
	 * @param context
	 */
	public static void deleteAllCookies(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		//cookieManager.removeSessionCookie();//移除用户信息
		//cookieManager.removeExpiredCookie();//移除生命周期信息
		CookieSyncManager.getInstance().sync();
	}
	/**
	 * 
	 * 添加指定cookie值
	 * @param context
	 * @param url
	 * @param cookies
	 */
	public static void setCookies(Context context, String url, String cookies) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.setCookie(url, cookies);//指定要修改的cookies
		CookieSyncManager.getInstance().sync();
	}
	
	/**
	 * 检查字符串是否为<code>null</code>或空字符串<code>""</code>。
	 * <pre>
	 * StringUtil.isEmpty(null)      = true
	 * StringUtil.isEmpty("")        = true
	 * StringUtil.isEmpty(" ")       = true
	 * StringUtil.isEmpty("bob")     = false
	 * StringUtil.isEmpty("  bob  ") = false
	 * </pre>
	 *
	 * @param str 要检查的字符串
	 *
	 * @return 如果为空, 则返回<code>true</code>
	 */
	public static boolean isEmpty(String str) {
		return ((str == null) || TextUtils.isEmpty(str.trim()));
	}
	
    /**
     * 检查URL中是否包含HTTP
     * @param url
     * @return true HTTP开头  false 不以HTTP为开头
     */
	public static boolean checkValidUrl(String url){
		if(!isEmpty(url) && url.toLowerCase().startsWith("http")){
			return true ;
		}
		return false ;
	}
	/**
	 * 将JSON格式转换为map集合
	 * @param cpsStr
	 * @return
	 */
	public static HashMap<String, String> getCpsMap(String cpsStr) {
		try {
			if (!StringUtil.isBlank(cpsStr)) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject jsonObject = new JSONObject(cpsStr);
				JSONArray names = jsonObject.names();
				for (int i = 0; i < names.length(); i++) {
					String key =  names.getString(i);
					String value = jsonObject.getString(key);
					map.put(key, value);
				}
				return map;
			}
		} catch (Exception e) {
			L.e(e);
		}
		return null;
	} 
	/**
	 * 获取URL中的参数
	 * @param url 
	 * @return 
	 */
	public static HashMap<String, String> getUrlParams(String url){
		if (!StringUtil.isBlank(url)) {
			try {
				String[] parameter = url.split("\\?");
				if (parameter!=null&&parameter.length>=2) {
					String[] params = parameter[1].split("&");
					HashMap<String, String> paramsMap = new HashMap<String, String>();
					for (int i = 0; i < params.length; i++) {
						String[] tmp = params[i].split("=");
						paramsMap.put(tmp[0], tmp[1]);
					}
					return paramsMap;
				}
			} catch (Exception e) {
				L.e(e);
			}
		}
		return null;
	}
	/**
	 * 跳转到内嵌WebView页面
	 * @param con
	 * @param title 标题(不传递该参数，默认不显示标题）
	 * @param url
	 */
	public static void goInnerWebview(Context con, String title, String url) {
  		Intent htmlIntent = new Intent(con,BaseWebviewActivity.class);
		if (!StringUtil.isBlank(title)) {
			htmlIntent.putExtra(BaseWebviewActivity.TITLECONTENT_INTENT_KEY,title);
		}
		if (!StringUtil.isBlank(url)) {
			htmlIntent.putExtra(BaseWebviewActivity.LOADURL_INTENT_KEY,url);
		}
		htmlIntent.putExtra(BaseWebviewActivity.BOTTOMBAR_INTENT_KEY,false);//是否显示底部栏,默认显示
		con.startActivity(htmlIntent);
	}
}