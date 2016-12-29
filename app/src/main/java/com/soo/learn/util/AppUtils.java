package com.soo.learn.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 跟App相关的辅助类
 * 获取app 名称 版本号 等
 * @author zhy
 * 
 */
public class AppUtils {

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 *
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 *
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	public static String getSystemProperty(String propName) {
		String line;
		BufferedReader input = null;
		try {
			Process p = Runtime.getRuntime().exec("getprop " + propName);
			input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
			line = input.readLine();
			input.close();
		} catch (IOException ex) {
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
		return line;
	}

	public static boolean isMIUIRom() {
		String property = getSystemProperty("ro.miui.ui.version.name");
		return !StringUtil.isEmpty(property);
	}
}

