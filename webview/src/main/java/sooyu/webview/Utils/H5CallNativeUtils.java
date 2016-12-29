package sooyu.webview.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.HashMap;

import sooyu.webview.config.H5CallNativeConstant;

public class H5CallNativeUtils {
	
	/** 获取用户信息*/
	public static String getuserinfoOld(Context con) {
		String res = "";
		try {
			JSONObject json = new JSONObject();
			//用户信息
	/*		SharedPreferences pref = con.getSharedPreferences(Constant.PUBLIC_FILE, 0);
			String userid = pref.getString(Constant.USER_USERID, "");// 用户id
			String isLogin = StringUtil.isEmpty(userid) ? "false" : "true";// 用户登录状态
			String guid = pref.getString(Constant.UID, "");// guid
			
			String uGuid = pref.getString(Constant.UGUID, "");//20151125新增GUID,用来存储新用户唯一值

			//设备信息
			String appPlatform = "android";// 客户端平台
			String appVersion = Tools.getAppVersionName(con);// 客户端版本
			String systemVersion = GetPhoneState.getSysRelease();// 系统版本
			String mobileModel = GetPhoneState.getModel();// 设备型号
			String imei = GetPhoneState.readTelephoneSerialNum(con);// 设备id（imei、idfa、mac等，唯一标识）
			int screenWidth = DeviceUtils.getScreenWidth(con);// 屏幕分辨率 - 宽
			int screenHeigh = DeviceUtils.getScreenHeight(con);// 屏幕分辨率 -

			//业务信息
			String sourceId = SourceidManager.getInstance(con)
					.getSourceid();// 客户端销售渠道号（ERP中的渠道）
			String cpsValue = H5SaveNativeUtils.getCps(con);// 客户端推广渠道号(CPS主渠道、子渠道、thkey值)
			
			String cartId=ShopcartHelper.getShopCartId(con);

			json.put(H5CallNativeConstant.H5_KEY_UID, userid);
			json.put(H5CallNativeConstant.H5_KEY_ISLOGIN, isLogin);

			json.put(H5CallNativeConstant.H5_KEY_APPPLATFORM, appPlatform);
			json.put(H5CallNativeConstant.H5_KEY_APPVERSION, appVersion);
			json.put(H5CallNativeConstant.H5_KEY_SYSTEMVERSION, systemVersion);
			json.put(H5CallNativeConstant.H5_KEY_MOBILEMODEL, mobileModel);
			json.put(H5CallNativeConstant.H5_KEY_IMEI, imei);
			json.put(H5CallNativeConstant.H5_KEY_GUID, guid);
			json.put(H5CallNativeConstant.H5_KEY_UGUID, uGuid);//20151125新增GUID,用来存储新用户唯一值
			json.put(H5CallNativeConstant.H5_KEY_SCREENWIDTH, screenWidth + "");
			json.put(H5CallNativeConstant.H5_KEY_SCREENHEIGH, screenHeigh + "");
			/*json.put(H5CallNativeConstant.H5_KEY_GPS_LATITUDE,
					MyApplication.getLatitude());// 纬度
			json.put(H5CallNativeConstant.H5_KEY_GPS_LONGITUDE,
					MyApplication.getLongitude());// 经度

			json.put(H5CallNativeConstant.H5_KEY_SOURCEID, sourceId);
			/*json.put(H5CallNativeConstant.H5_KEY_PRODUCTLINE,
					Constant.PRODUCT_LINE);// 产品线
			json.put(H5CallNativeConstant.H5_KEY_CPS, cpsValue);
			json.put(H5CallNativeConstant.H5_KEY_SHOP_CART_ID, cartId);//购物车数量
			//json.put(H5CallNativeConstant.H5_KEY_YT_VIP_OPEN_ID, MyApplication.getOpenId());//银泰贵宾卡唯一标示
			*/
			HashMap<String, String> map =new HashMap<>();
				//	(HashMap<String, String>)Tools.getDefaultMap(DataServer.getApplication().getApplicationContext());
			if (map!=null&&map.size()>0) {
				json.put(H5CallNativeConstant.H5_KEY_MACID, map.get(H5CallNativeConstant.H5_KEY_MACID));//设备Mac地址
				json.put(H5CallNativeConstant.H5_KEY_CARRIER, map.get(H5CallNativeConstant.H5_KEY_CARRIER));//设备运营商
				String yintaiSourceId = "";
				if (map.containsKey(H5CallNativeConstant.H5_KEY_YINTAISOURCEID)) {//渠道信息
					yintaiSourceId = map.get(H5CallNativeConstant.H5_KEY_YINTAISOURCEID);
				}
				json.put(H5CallNativeConstant.H5_KEY_YINTAISOURCEID, yintaiSourceId);
			}
			res = json.toString();
			L.d(res);
		} catch (Exception e) {
			L.e(e.getMessage());
		}
		return res;
	}

	/** 获取用户信息(新版)*/
	public static String getuserinfo(Context con) {
		String returnJson = "";
		/*try {
			H5GetUserBean returnBean =new H5GetUserBean();

			//用户信息
			SharedPreferences pref = con.getSharedPreferences(Constant.PUBLIC_FILE, 0);
			String userid = pref.getString(Constant.USER_USERID, "");// 用户id
			String isLogin = StringUtil.isEmpty(userid) ? "false" : "true";// 用户登录状态
			String guid = pref.getString(Constant.UID, "");// guid
			String userTokenId=pref.getString(Constant.USER_TOKENID,"");

			String uGuid = pref.getString(Constant.UGUID, "");//20151125新增GUID,用来存储新用户唯一值
			String partnerType = pref.getString(Constant.PARTNER_TYPE, "");
			String partnerOpenId = pref.getString(Constant.OPENID, "");

			//设备信息
			String appPlatform = "android";// 客户端平台
			String appVersion = Tools.getAppVersionName(con);// 客户端版本
			String systemVersion = GetPhoneState.getSysRelease();// 系统版本
			String mobileModel = GetPhoneState.getModel();// 设备型号
			String imei = GetPhoneState.readTelephoneSerialNum(con);// 设备id（imei、idfa、mac等，唯一标识）
			int screenWidth = DeviceUtils.getScreenWidth(con);// 屏幕分辨率 - 宽
			int screenHeigh = DeviceUtils.getScreenHeight(con);// 屏幕分辨率 -

			//业务信息
			String sourceId = SourceidManager.getInstance(con).getSourceid();// 客户端销售渠道号（ERP中的渠道）
			String cpsValue = H5SaveNativeUtils.getCps(con);// 客户端推广渠道号(CPS主渠道、子渠道、thkey值)
			String cartId=ShopcartHelper.getShopCartId(con);

			returnBean.setUserTokenId(userTokenId);
			returnBean.setUserid(userid);
			returnBean.setUserstate(isLogin);
			returnBean.setOs(appPlatform);
			returnBean.setClientv(appVersion);
			returnBean.setOsversion(systemVersion);
			returnBean.setDevicename(mobileModel);
			returnBean.setImei(imei);
			returnBean.setGuid(guid);
			returnBean.setPartnerType(partnerType);//add 4.5.0 登录类型
			returnBean.setOpenId(partnerOpenId);//add 4.5.0 登录的第三方平台openid
			returnBean.setUguid(uGuid);//20151125新增GUID,用来存储新用户唯一值
			returnBean.setScreenWidth(screenWidth+ "");
			returnBean.setScreenHeigh(screenHeigh+ "");
			returnBean.setGpsx(MyApplication.getLatitude());// 纬度
			returnBean.setGpsy(MyApplication.getLongitude());// 经度
			returnBean.setSource(sourceId);
			returnBean.setApptype(Constant.PRODUCT_LINE);// 产品线
			returnBean.setCps(cpsValue);
			returnBean.setCartId(cartId);//购物车数量
			returnBean.setYtVipOpenID(MyApplication.getOpenId());   //银泰贵宾卡唯一标示
			HashMap<String, String> map =(HashMap<String, String>)Tools.getDefaultMap(DataServer.getApplication().getApplicationContext());
			if (map!=null&&map.size()>0) {
				returnBean.setMacid(map.get(H5CallNativeConstant.H5_KEY_MACID));//设备Mac地址
				returnBean.setCarrier(map.get(H5CallNativeConstant.H5_KEY_CARRIER));
				String yintaiSourceId = "";
				if (map.containsKey(H5CallNativeConstant.H5_KEY_YINTAISOURCEID)) {//渠道信息
					yintaiSourceId = map.get(H5CallNativeConstant.H5_KEY_YINTAISOURCEID);
				}
				returnBean.setYintaisourceId(yintaiSourceId);
			}
			returnJson = UGson.toJson(returnBean);
			TransfersLog.d(returnJson);
		} catch (Exception e) {
			TransfersLog.e(e.getMessage());
		}*/
		return returnJson;
	}

	
	public static void setViewEnabled(View view, boolean isBlock){
		if (view!=null) {
			view.setEnabled(isBlock);
			view.setFocusable(isBlock);
			if(view instanceof ViewGroup){
				ViewGroup viewGroup= (ViewGroup) view;
				for ( int i = 0 ; i < viewGroup.getChildCount(); i++ ) {
					viewGroup.getChildAt(i).setEnabled(isBlock);
					viewGroup.getChildAt(i).setFocusable(isBlock);
				}
			}
		}
	}
}
