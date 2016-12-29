package sooyu.webview.Utils;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


/**URL辅助工具类*/
public class WebUrlUtils {

	public static final String TAG =WebUrlUtils.class.getName();
	
	/**
	 * 获取URL中指定key的参数 
	 * @param webUrl
	 * @return
	 */
	public static String getParam(String webUrl, String key){
		try {
			if (!StringUtil.isBlank(webUrl)) {
				Uri uri = Uri.parse(StringUtil.f(webUrl));
				if (uri!=null) {
					return uri.getQueryParameter(key);
				}
			}
		} catch (Exception e) {
			L.e(TAG, e.getMessage());
		}
		return null ;
	}
	
	/**
	 * 解析URL中的参数,并以map返回
	 * @param webUrl
	 * @return
	 */
	public static HashMap<String, String> getParams(String webUrl){
		/*try {
			if (!StringUtil.isBlank(webUrl)) {
				URI uri = new URI(StringUtil.f(webUrl));
				if (uri!=null) {
					List<NameValuePair> paramsList = URLEncodedUtils.parse(uri,  "UTF-8");
					//只有当跳转类型被客户端识别时才处理参数
					if(!CListUtil.isEmpty(paramsList)){
						HashMap<String, String> paramMap = new HashMap<String, String>();
						//转换参数
						for( NameValuePair nvp: paramsList ){
							if(!StringUtil.isEmpty(nvp.getName()) &&!StringUtil.isEmpty(nvp.getValue())){
								paramMap.put(nvp.getName(), nvp.getValue());
							}
						}
						return paramMap;
					}
				}
			}
		} catch (Exception e) {
			L.e(TAG, e.getMessage());
		}*/
		return null ;
	}

	public static URI convert2URI(String webUrl){
		try {
			URI uri = new URI(StringUtil.f(webUrl));
			return uri;
		} catch (URISyntaxException e) {
			L.e(e);
		}
		return null;
	}

	public static String getScheme(String webUrl){
		if(StringUtil.isNotEmpty(webUrl)){
			URI uri = convert2URI(webUrl);
			if(uri!=null&&StringUtil.isNotEmpty(uri.getScheme())){
				return uri.getScheme();
			}
		}
		return "";
	}
	public static String getCallCommand(String webUrl){
		if(StringUtil.isNotEmpty(webUrl)){
			URI uri = convert2URI(webUrl);
			if(uri!=null&&StringUtil.isNotEmpty(uri.getHost())){
				return uri.getHost();
			}
		}
		return "";
	}

	public static String getJsonValue(String jsonStr, String key){
		if(StringUtil.isNotEmpty(jsonStr)&&StringUtil.isNotEmpty(key)){
			JSONObject json = DataConvertUtils.str2JsonObj(jsonStr);
			if (json!=null&&json.has(key)) {
				try {
					String keyValue = json.getString(key);
					return keyValue;
				} catch (JSONException e) {
				}
			}
		}
		return "";
	}
}