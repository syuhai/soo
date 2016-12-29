package sooyu.webview.Utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class H5SaveNativeUtils {
	private static final String CPS = "cps";
	private static final String CPS_STORAGECYCLE = "cps_storagecycle";
	public static final String KEY_CPSSOURCE = "cpssource";
	public static final String KEY_CPSSUBSOURCE = "subcpssource";
	public static final String KEY_THKEY = "thkey";
	public static final String KEY_SAVETIMESTAMP = "savetimestamp";
	/**秒*/
    public static final long KEY_SECOND = 1000L;
    /**分钟*/
    public static final long KEY_MINUTE = 60L*KEY_SECOND;
    /**小时*/
    public static final long KEY_HOUR = 60L* KEY_MINUTE;
    /**天*/
    public static final long KEY_DAY = 24L* KEY_HOUR;
    
	/**当前默认设定保存周期为30天<br>
	 * 因为服务器回传单位是小时，处理的时候要注意*/
	public static final long DEFAULT_STORAGECYCLE = KEY_DAY*30;
	
	/**
	 * 添加CPS值存储到客户端
	 * @param context
	 * @param value CPS值
	 */
	public static void setCps(Context context, String value) {
		if (StringUtil.isBlank(value)) {
			value="";
		}else {
			try {
				JSONObject jsonObject = new JSONObject(value);
				if (!jsonObject.has(KEY_SAVETIMESTAMP)) {
					jsonObject.put(KEY_SAVETIMESTAMP, System.currentTimeMillis());
					value = jsonObject.toString();
				}
			} catch (Exception e) {
				L.e(e);
			}
		}
		//SharedPreferencesTools.getInstance(context).putString(CPS, value);
	} 
	
	/**
	 * 从客户端获取CPS值
	 * @param context
	 * @return
	 */
	public static String getCps(Context context) {
		/*//处理CPS信息的存储，如果大于30天，则清除CPS信息
		String cpsJsonStr = SharedPreferencesTools.getInstance(context).getString(CPS);
		if (!StringUtil.isBlank(cpsJsonStr)) {
			try {
				JSONObject jsonObject = new JSONObject(cpsJsonStr);
				long currentTimeMillis = System.currentTimeMillis();
				long saveTimeStamp = jsonObject.getLong(KEY_SAVETIMESTAMP);
				long tmpTime = getCpsStorageCycle(context)*KEY_HOUR;//转换为毫秒
				if (tmpTime==0) {
					tmpTime = DEFAULT_STORAGECYCLE;
				}
				//判断存储的CPS是否大小指定周期，如果大于周期，则删除
				if ((currentTimeMillis-saveTimeStamp)>tmpTime) {
					setCps(context, "");
					cpsJsonStr="";
				}
			} catch (Exception e) {
				YTLog.e(e);
			}
		}*/
		return "";
	} 
	
	/**
	 * 按格式解析CPS值字符串
	 * @param context
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
	 * 
	 * @param context
	 * @param cpsStr
	 */
	public static void saveUrlParams(Context context, String cpsStr){
		if (!StringUtil.isBlank(cpsStr)) {
			//参数cps=cpssource:subcpssource:thkey
			//http://item.yintai.com/20-294-0320C.html?cps=1091002:10910111:acsdkljfskjldkjls
			String[] parameter = cpsStr.split("\\?");
			if (parameter!=null&&parameter.length>=2) {
				cpsStr = parameter[1];
				if (!StringUtil.isBlank(cpsStr)) {
					try {
						String[] params = cpsStr.split("&");
						for (int i = 0; i < params.length; i++) {
							String values = params[i];
							String[] split = values.split("=");
							String key = split[0];
							String value = split[1];
							if (key.equals("cps")) {
								JSONObject jsonObject = new JSONObject();
								String[] keys = value.split(":");
								jsonObject.put(KEY_CPSSOURCE, keys[0]);
								jsonObject.put(KEY_CPSSUBSOURCE, keys[1]);
								jsonObject.put(KEY_THKEY, keys[2]);
								jsonObject.put(KEY_SAVETIMESTAMP, System.currentTimeMillis());
								setCps(context, jsonObject.toString());
								L.i("cps", jsonObject.toString());
								break;
							}
						}
					} catch (Exception e) {
						L.e(e);
					}
				}
			}
		}
	}
	
	/**
	 * 获取CPS生命周期
	 * @param context
	 * @param
	 */
	public static long getCpsStorageCycle(Context context) {
		return  11;
		//return SharedPreferencesTools.getInstance(context).getLong(CPS_STORAGECYCLE);
	} 
	
	/**
	 * 保存CPS生命周期(存储的单位为小时)
	 * @param context
	 * @param value
	 */
	public static void setCpsStorageCycle(Context context, long value) {
		//SharedPreferencesTools.getInstance(context).putLong(CPS_STORAGECYCLE, value);
	} 
	
}