/*******************************************************************************
 * @project: 
 * @file: GsonUtil.java
 * @author: Cuckoo
 * @created: 2012-05-6
 * @purpose:
 *
 * @version: 1.0
 *
 * Revision History at the end of file.
 *
 * Copyright 2012 Cuckoo All rights reserved.
 ******************************************************************************/
package sooyu.webview.Utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Type;

public class GsonUtil {
	
	private static volatile Gson gson = null ;
	
	private GsonUtil(){
	}
	
	private static Gson getGson(){
		if( gson == null ){
			synchronized (GsonUtil.class) {
				if( gson == null ){
					gson = new Gson() ;
				}
			}
		}
		return gson ;
	}
	
	/**
	 * Transfer the java bean to JSON string. 
	 * Notice the bean mast can be serialize.
	 * @param bean
	 * @return
	 */
	public static String toJson(Object bean){
		return getGson().toJson(bean);
	}
	
	/**
	 * 将bean转成数组形式
	 * @param bean
	 * @param typeOfSrc
	 * 		Type typeOfSrc = new TypeToken<ArrayList<Object>>(){}.getType();
	 * @return
	 */
	public static String toJson(Object bean,Type typeOfSrc){
		return getGson().toJson(bean, typeOfSrc);
	}
	
	
	/**
	 * parse JSON to java bean
	 * @param json
	 * @param objClass
	 * @return
	 */
	public static <T> Object parseJson(String json, Class<T> objClass){
		if( !StringUtil.isEmpty(json) ){
			try{
				return getGson().fromJson(json, objClass);
			}catch (JsonSyntaxException e) {
				Log.e("", "Parse json error: " + e.getMessage());
			}
		}
		return null ;
	}
	
	/**
	 * 
	 * @param json
	 * @param typeOfT
	 * 	Type typeOfSrc = new TypeToken<ArrayList<Object>>(){}.getType();
	 * @return
	 */
	public static <T> Object fromJson(String json,  Type typeOfT){
		if( !StringUtil.isEmpty(json) ){
			try{
				return getGson().fromJson(json, typeOfT);
			}catch (JsonSyntaxException e) {
				Log.e("", "Parse json error: " + e.getMessage());
			}
		}
		return null ;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String json, Class<T> type){
		if(json == null){
			return null;
		}
		JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);		
		return (T)gson.fromJson(reader, TypeToken.get(type).getType());
	}
}
/*******************************************************************************
 * Revision History [type 'revision' & press Alt + '/' to insert revision block]
 * 
 * [Revision on 2012-5-6 17:44:09 by Cuckoo]<BR>
 * Create a util for parse and packaging JSON .
 * Copyright 2011 Cuckoo Systems All rights reserved.
 ******************************************************************************/