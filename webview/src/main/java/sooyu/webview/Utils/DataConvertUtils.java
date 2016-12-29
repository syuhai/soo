package sooyu.webview.Utils;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DataConvertUtils {
	
	
	/**
	 * 把ItemCodes 字符数组按指定格式输出  [1111,121122]
	 * @param itemcodes
	 * @return
	 */
	public static String initString(String[] itemcodes){
		if (itemcodes!=null&&itemcodes.length>0) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (int i = 0; i < itemcodes.length; i++) {
				sb.append("\""+itemcodes[i]+"\"");
				if (i<(itemcodes.length-1)) {
					sb.append(",");
				}
			}
			sb.append("]");
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * 把String[] 字符数组按指定格式输出 1111,121122
	 * @param
	 * @return
	 */
	public static String initString(ArrayList<String> array, String separator){
		if (!isNullArrayList(array)) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < array.size(); i++) {
				sb.append(array.get(i));//sb.append("\""+array[i]+"\"");
				if (i<(array.size()-1)) {
					sb.append(",");
				}
			}
			return sb.toString();
		}
		return "";
	}
	
	public static JSONObject str2JsonObj(String JsonStr){
		if(!StringUtil.isEmpty(JsonStr)){
			try {
				JSONObject json = new JSONObject(JsonStr);
				return json;
			} catch (JSONException e) {
				L.e(e);
			}
		}
		return null;
	}
	
	public static boolean isExtrasEmpty(Intent intent){
		if (intent!=null&&intent.getExtras()!=null&&!intent.getExtras().isEmpty()) {
			return false;
		}
		return true;
	}
	public static Bundle setIntentStr(Intent intent, String key, String value){
		if(intent==null){
			intent = new Intent();
		}
		return setBundleStr(intent.getExtras(), key,value);
	}
	
	public static Bundle setBundleStr(Bundle extras, String key, String value){
		if (extras==null) {
			extras = new Bundle();
		}
		extras.putString(key, value);
		return extras;
	}
	public static String getIntentStr(Intent intent, String key){
		if(intent!=null){
			return getBundleStr(intent.getExtras(), key);
		}
		return null;
	}
	
	public static String getBundleStr(Bundle extras, String key){
		if (extras!=null&&!extras.isEmpty()) {
			if (extras.containsKey(key)) {
				return extras.getString(key);
			}
		}
		return null;
	}
	
	public static void removeIntentStr(Intent intent, String key){
		if(intent!=null){
			removeBundleStr(intent.getExtras(), key);
		}
	}
	public static void removeBundleStr(Bundle extras, String key){
		if (extras!=null&&!extras.isEmpty()) {
			if (extras.containsKey(key)) {
				extras.remove(key);
			}
		}
	}
	
	public static <T> T getListData(List<T> list, int index){
		if (!isNullArrayList(list)) {
			if (list.size()>index) {
				return list.get(index);
			}
		}
		return null;
	}
	
	public static boolean isNullArrayList(List<?> list){
		if (list!=null&&list.size()>0) {
			return false;
		}
		return true;
	}
	
	/**转换String为ArrayList<String>*/
	public ArrayList<String> getStringList(String id) {
		if (StringUtil.isBlank(id)) {
			return null;
		}
		ArrayList<String> returnList = new ArrayList<String>();
		returnList.add(id);
		return returnList;
	}
	/**将bean中的指定字段，转换为ArrayList<String>*//*
	public ArrayList<String> convertStringList(ArrayList<GetScopeListResponse.ResponseData> list) {
		if (DataConvertUtils.isNullArrayList(list)) {
			return null;
		}
		ArrayList<String> returnList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			GetScopeListResponse.ResponseData bean = list.get(i);
			if (bean!=null&&!StringUtil.isBlank(bean.name)) {
				returnList.add(bean.name);
			}
		}
		return returnList;
	}*/
	public static void showData(Object...  str){
		if (str!=null) {
			L.d("ModuleViewManage", str.toString());
		}
	}
}