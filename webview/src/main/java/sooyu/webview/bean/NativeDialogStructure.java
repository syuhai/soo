package sooyu.webview.bean;


import java.io.Serializable;

import sooyu.webview.interfaces.impl.H5CallNativeIMPL;

/**
 * h5页面调用原生的基本的弹出消息框，并支持定义标题，内容，按钮内容，按钮事件的操作
 */
public class NativeDialogStructure implements Serializable {
	/**
	 * 字段或域定义：<code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -39465282985750875L;
	private int mWhat;
	private int mTime;
	private String mTitle;
	private String mMessage;
	private String mLeftStr;
	private String mLeftListener;
	private String mRightStr;
	private String mRightListener;
	private H5CallNativeIMPL mNativeHtml5;

	public NativeDialogStructure() {

	}

	public int getWhat() {
		return mWhat;
	}

	public void setWhat(int mWhat) {
		this.mWhat = mWhat;
	}

	public int getTime() {
		return mTime;
	}

	public void setTime(int mTime) {
		this.mTime = mTime;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public String getLeftStr() {
		return mLeftStr;
	}

	public void setLeftStr(String mLeftStr) {
		this.mLeftStr = mLeftStr;
	}

	public String getLeftListener() {
		return mLeftListener;
	}

	public void setLeftListener(String mLeftListener) {
		this.mLeftListener = mLeftListener;
	}

	public String getRightStr() {
		return mRightStr;
	}

	public void setRightStr(String mRightStr) {
		this.mRightStr = mRightStr;
	}

	public String getRightListener() {
		return mRightListener;
	}

	public void setRightListener(String mRightListener) {
		this.mRightListener = mRightListener;
	}

	public H5CallNativeIMPL getNativeHtml5() {
		return mNativeHtml5;
	}

	public void setNativeHtml5(H5CallNativeIMPL mNativeHtml5) {
		this.mNativeHtml5 = mNativeHtml5;
	}
}
