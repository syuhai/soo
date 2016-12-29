package sooyu.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ConsoleMessage.MessageLevel;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import sooyu.webview.Utils.DataConvertUtils;
import sooyu.webview.Utils.FileUtils;
import sooyu.webview.Utils.H5CallNativeUtils;
import sooyu.webview.Utils.H5WebViewHelper;
import sooyu.webview.Utils.JSBrigeWebViewClient;
import sooyu.webview.Utils.L;
import sooyu.webview.Utils.StringUtil;
import sooyu.webview.Utils.SystemSupportUtils;
import sooyu.webview.bean.NativeDialogStructure;
import sooyu.webview.config.H5CallNativeConstant;
import sooyu.webview.interfaces.impl.H5CallNativeIMPL;
import sooyu.webview.pulltorefresh.PullToRefreshBase;
import sooyu.webview.pulltorefresh.PullToRefreshWebView;
import sooyu.webview.view.YTUploadPicDialog;
import sooyu.webview.webviewjavascriptbridge.WVJBWebViewClient;


/**
 * WebView Base Activity<br>
 * 1.直接支持显示html5<br>
 * intent.putExtra(BaseWebviewActivity.LOADURL_INTENT_KEY,"URL网址");<br>
 * intent.putExtra(BaseWebviewActivity.TITLECONTENT_INTENT_KEY,"title显示内容");//不传递该参数，默认不显示标题<br>
 * intent.putExtra(BaseWebviewActivity.BOTTOMBAR_INTENT_KEY,true);//是否显示底部栏,默认显示<br>
 */
public class BaseWebviewActivity extends Activity{
	
	public static String TAG = "webview";
	/**显示网址*/
	public static String LOADURL_INTENT_KEY = "url";
	/**title显示*/
	public static String TITLECONTENT_INTENT_KEY = "title";
	//是否允许下拉刷新，1允许，0不允许
	public static String ALLOW_REFRESH_KEY ="allowRefresh";
	/**是否允许缩放，1允许，0不允许，默认不允许*/
	public static String KEY_ALLOW_ZOOM ="allowZoom";
	/**是否显示底部栏*/
	public static String BOTTOMBAR_INTENT_KEY = "bottombar";
	public static final String KEY_TITLEBAR_RIGHT_SHARED="yintaiappsharebtn";
	
	/** 关闭页面设置result intent传递数据 Key */
	public static String ACTIVITY_FOR_RESULT_INTENT_KEY_REFRESH="Refresh_Previous_Page";
	/** 跳转到BaseWebviewActivity用startActivityForResult Request/Result Code */
	public static final int BASE_WEBVIEW_RESULT_CODE=10010;
	//下拉刷新状态-禁止下拉刷新
	private final String STATUS_ALLOWREFRESH_DISABLE = "0";
	/**缩放状态-开启缩放*/
	private final String STATUS_ALLOWZOOM_ENABLE = "1";

	protected PullToRefreshWebView mPullRefreshWebView;
	protected WebView mWebView;
	protected ProgressBar mProgressBar;
	protected RelativeLayout layout_loading;
	protected View headLayout;
	protected View bodyLayout;
	protected WVJBWebViewClient webViewClient;
	
	/** 是否设置进度条*/
	protected boolean isSetWebChromeClient = false ;
	/**Flag - 设置是否由来h5来操作返回 */
	protected boolean isResponseClickBack = false;
	protected boolean isBlock = false;
	protected String url = null;
	//支付跳转url
	private String payJumpUrl;

	/** 文件选择/上传 */
	private ValueCallback<Uri> mUploadMessage;
	public static final int FILECHOOSER_RESULTCODE = 21001;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;
	String compressPath = "";
	private String imagePaths;
	private Uri cameraUri;
    /**对话框.*/
    private YTUploadPicDialog menuDialog  = null;
    /** 
     * 区分关闭dialog是去选择图片还是仅仅关闭dialog <br>
     * true  关闭dialog并去选择图片 <br>
     * false 仅仅关闭dialog<br>
     */
    private boolean isDialogColse2ChoisePic=false;

    /** WebView所在的布局 */
    private FrameLayout mFLWebView=null;
    /** 网页视频全屏布局 */
	private FrameLayout mFLH5FullScreenVideoView =null;
	/**  */
	private WebChromeClient mChromeClient = null;
	/** 网页视屏全屏播放回调传的View，用于判断状态 */
	private View mH5FullScreenVideoView = null;
	/**  */
	private WebChromeClient.CustomViewCallback mWCCCustomViewCallBack = null;
	
	protected void initialize() {
		boolean isShowTitileBar = true;
		boolean isShowBottomBar = true;
		Intent passIntent = getIntent();
		if (passIntent!=null) {
			Bundle passBundle = passIntent.getExtras();
			if (passBundle!=null&&passBundle.size()>0) {
				//isShowTitileBar = passBundle.containsKey(TITLECONTENT_INTENT_KEY);//可以动态设置是否显示标题栏
				if (passBundle.containsKey(BOTTOMBAR_INTENT_KEY)) {
					isShowBottomBar = passBundle.getBoolean(BOTTOMBAR_INTENT_KEY);
				}
			}
		}
	}
/*	@Override
	protected View createHead() {
		headLayout = super.createHead();
		return headLayout;
	}*/
	
	protected View createLinearBody() {
		bodyLayout = LayoutInflater.from(this).inflate(R.layout.h5callnative_layout_body_refresh, null, false);
		mFLWebView = (FrameLayout) bodyLayout.findViewById(R.id.web_fl);
		mFLH5FullScreenVideoView = (FrameLayout) bodyLayout.findViewById(R.id.video_fl);
		layout_loading = (RelativeLayout) bodyLayout.findViewById(R.id.webview_loading);
		//mProgressBar = (ProgressBar) bodyLayout.findViewById(R.id.progressbar);
		//mWebView = (WebView) bodyLayout.findViewById(R.id.webview);
		
		mPullRefreshWebView = (PullToRefreshWebView) bodyLayout.findViewById(R.id.pull_refresh_webview);
		mWebView = mPullRefreshWebView.getRefreshableView();
		return bodyLayout;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	/**
	 * 设置webview的刷新模式
	 * @param isSupport true支持 false不支持
	 */
	protected void setWebViewRefreshSupport(boolean isSupport) {
		if (mPullRefreshWebView==null) {
			return;
		}
		if (isSupport) {
			mPullRefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//开启
		}else {
			mPullRefreshWebView.setMode(PullToRefreshBase.Mode.DISABLED);//禁止
		}
	}
	
	protected void process(Bundle savedInstanceState) {
		L.d(TAG, "BasicWebviewActivity ... handleCreate");
		String tempStr = DataConvertUtils.getIntentStr(getIntent(), TITLECONTENT_INTENT_KEY);
		url = getIntent().getStringExtra(LOADURL_INTENT_KEY);
		//默认允许下拉刷新
		String allowRefreshStatus = getIntent().getStringExtra(ALLOW_REFRESH_KEY);
		boolean isAllowRefresh = true ;
		if(STATUS_ALLOWREFRESH_DISABLE.equals(allowRefreshStatus)){
			isAllowRefresh = false ;
		}

		if( isAllowRefresh ){
			//允许下拉刷新
			setWebViewRefreshSupport(true);//设置webview的刷新模式
		}else {
			//禁止下拉刷新
			setWebViewRefreshSupport(false);//设置webview的刷新模式

		}
		if (StringUtil.isBlank(tempStr)) {
			tempStr = getString(R.string.app_name);
		}
		//setTitleInfo(tempStr);
		initWebView();
		if (!H5WebViewHelper.isEmpty(url)) {
			loadUrl(url);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyWebView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			//网页视频全屏播放回调View不为空，证明正在网页视频正在全屏状态
			//需要先关闭全屏
			if(null!=mH5FullScreenVideoView){
				mChromeClient.onHideCustomView();
				return true;
			}else if (mWebView != null && mWebView.canGoBack()) {
				//采用webview默认的回退
				mWebView.goBack();
				return true;
			}
			//和左侧返回按钮处理方式保持一致
			//onClickTitleBarLeftBtn();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initWebView() {
		L.d(TAG,"BasicWebviewActivity.initWebView" + mWebView.toString());
		mWebView = H5WebViewHelper.initWebView(mWebView);
		String userAgentStr = " YintaiApp/android/";
		String appVersion ="appName"; //Appu.getAppVersionName(BaseWebviewActivity.this);
		if(!StringUtil.isEmpty(appVersion)){
			userAgentStr+=appVersion;
		}
		H5WebViewHelper.setUserAgentString(mWebView,userAgentStr);
		mWebView.addJavascriptInterface(
				H5CallNativeIMPL.getInstance(this, mWebView),
				H5CallNativeConstant.H5_CALL_NATIVE_JSINTERFACE);
		 
		mWebView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent,
										String contentDisposition, String mimetype, long contentLength) {
				L.i(TAG, "url="+url+"  userAgent="+userAgent+"  contentDisposition="+contentDisposition+
						"  mimetype="+mimetype+"  contentLength="+contentLength);              
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);              
			}
		});
		//if(isSetWebChromeClient){//如果开启下拉刷新，不能覆盖WebChromeClient，因为会覆盖掉原有的刷新完成机制
			// 设置webview进度条
		mChromeClient=new BaseWebChromeClient();
		mWebView.setWebChromeClient(mChromeClient);
		//}
		webViewClient = new JSBrigeWebViewClient(mWebView,this);
		mWebView.setWebViewClient(webViewClient);
		//webViewClient.injectionJS();
		clearCache();
		// setHtml5Cache();
		//默认不允许缩放
		String allowZoom = getIntent().getStringExtra(KEY_ALLOW_ZOOM);
		if(STATUS_ALLOWZOOM_ENABLE.equals(allowZoom)){
			setAllowZoomEnable(true);
		}
	}
	/**缩放状态-开启缩放 true 开启  false 关闭*/
	public void setAllowZoomEnable(boolean isAllowZoom) {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setSupportZoom(isAllowZoom);
	}
	
	/**清除WebView的历史记录及缓存*/
	public void clearCache() {
		if (mWebView!=null) {
			mWebView.clearHistory();
			mWebView.clearCache(true);
			mWebView.clearFormData();
		}
	}

	public Handler basicHandler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	/**释放WebView BasicWebviewActivity.destroyWebView()*/
	public void destroyWebView() {
		if (mWebView != null) {
			mWebView.setVisibility(View.GONE);
			mWebView.stopLoading();
			//root.removeView(mWebView);//error nullpointerexception
			mWebView.removeAllViews();
			mWebView.destroy();
			mWebView = null;
		}
	}

	/**
	 * 加载网址 BasicWebviewActivity.loadUrl()<BR>
	 * @param url
	 */
	public void loadUrl(String url) {
		L.d(url);
		mWebView.loadUrl(url);
	}

	/**
	 * 设置APP cache BasicWebviewActivity.setHtml5Cache()<BR>
	 * @param pAppDirName
	 * @param pAppCacheSize
	 */
	public void setHtml5Cache(String pAppDirName, long pAppCacheSize) {
		String cacheDir = getDir(pAppDirName, Context.MODE_WORLD_WRITEABLE).getAbsolutePath();
		WebSettings settings = mWebView.getSettings();
		settings.setAppCachePath(cacheDir);
		settings.setAppCacheEnabled(true);
		settings.setAppCacheMaxSize(pAppCacheSize);
	}

	public WebView getWebView() {
		return mWebView;
	}
	public void sendMessage(int what) {
		sendMessage(what, 0, 0, null);
	}

	public void sendMessage(int what, Object data) {
		sendMessage(what, 0, 0, data);
	}

	public void sendMessage(int what, int arg1, int arg2, Object data) {
		if (basicHandler != null) {
			Message message = basicHandler.obtainMessage(what, arg1, arg2);
			message.obj = data;
			basicHandler.sendMessage(message);
		}
	}
	
	public void showH5_2_androidDialog(NativeDialogStructure nds) {
		if (basicHandler != null) {
			Message message = basicHandler.obtainMessage(nds.getWhat());
			message.obj = nds;
			basicHandler.sendMessage(message);
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		//继续播放网页上的视频
		H5WebViewHelper.setWebViewResume(mWebView);
		H5CallNativeIMPL.getInstance(this, mWebView);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//停止播放网页上的视频
		H5WebViewHelper.setWebViewPause(mWebView);
	}
	
	private void jumpActivity(String jumpStr){
		//JumpCtrler.doJump(this,jumpStr);
	}

	protected void onClickClient(View v) {}


	public boolean handleMessage(Message msg) {
		if (msg.what==H5CallNativeConstant.H5CALLNATIVE_STARTACTIVITY_MSGID) {
			//跳转到对应activity的对接
			jumpActivity((String) msg.obj);
			return false;
		}else if(msg.what==H5CallNativeConstant.H5CALLNATIVE_SHOWDIG_MSGID) {
			//showErrorDialog((String) msg.obj);
			return false;
		}else if(msg.what==H5CallNativeConstant.H5CALLNATIVE_SHOWPROGRESSDLG_MSGID) {
			//showLoaddingView();
			return false;
		}else if(msg.what==H5CallNativeConstant.H5CALLNATIVE_DISSPROGRESSDLG_MSGID){
			dismissLoaddingView();
			return false;
		}else if(msg.what==H5CallNativeConstant.H5CALLNATIVE_SETTITLEBAR_MSGID){
			setTitleBar((String) msg.obj);
			return false;
		}else if(msg.what==H5CallNativeConstant.H5CALLNATIVE_REFRESH_PREVIOUS_PAGE_MSGID){
			setRefreshPreviousPage();
			return false;
		}else if(msg.what==H5CallNativeConstant.H5CALLNATIVE_LOAD_START_MSGID){
			isResponseClickBack = false;
			L.i(TAG,"reset isResponseClickBack:value is "+isResponseClickBack);
			return false;
		}
		return true;
	}


	/** 刷新上一个页面 */
	public void setRefreshPreviousPage(){
		Intent intent=new Intent();
		intent.putExtra(ACTIVITY_FOR_RESULT_INTENT_KEY_REFRESH, true);
		setResult(BASE_WEBVIEW_RESULT_CODE,intent);;
	}
	
	public void setTitleBar(String params) {
		/*JumpURI uri = SchemeParser.parseJumpParams(params);
		if( uri == null ){
			//不支持的类型
			YTLog.e("not support type");
			return ;
		}
		HashMap<String,String> paramMap = uri.getParamMap();
		JumpType jumpType = uri.getJumpType();
		if (JumpType.ShopCarCount == jumpType) {
			//示例：yintaimobile://ShopCarCount?count=3
			if (paramMap.containsKey("count")) {
				String count = paramMap.get("count");
				editShopCarNumber(count);
			}
		}else if (JumpType.ShieldTopBottom==jumpType) {
			//yintaimobile://ShieldTopBottom?state=true
			if (paramMap.containsKey("state")) {
				String state = paramMap.get("state");
				boolean isBlock  = "true".equalsIgnoreCase(state)?true:false;
				blockTopBottomBar(isBlock);
			}
		}else if (JumpType.SetTitleBar==jumpType) {
			String position = paramMap.containsKey("position")?paramMap.get("position"):"";
			Boolean isShow = paramMap.containsKey("isShow")? Boolean.parseBoolean(paramMap.get("isShow").toLowerCase()):false;
			String value = paramMap.containsKey("value")?paramMap.get("value"):"";
			setNativeTitleBar(position,isShow,value);
		}*/
	}
	
	/**
	 * 刷新消息数量
	 * @param num
	 */
	private void editShopCarNumber(String num) {
		int count = StringUtil.parseInt(num);
		if( count != -1&&StringUtil.isNumeric(num)){
			//ShopcartHelper.saveShopQuantity(this, Integer.valueOf(num));//更新本地购物车商品数量
		}
	}
	
	public void isResponseClickBack() {
		isResponseClickBack = true;
		L.i(TAG,"h5 call isResponseClickBack:value is "+isResponseClickBack);
	}

	/**是否由H5来处理标题栏左边的按钮触发事件
	 * @param isResponse
     */
	public void isResponseClickBack(String isResponse) {
		if ("true".equalsIgnoreCase(isResponse)) {
			isResponseClickBack = true;
		}else if ("false".equalsIgnoreCase(isResponse)){
			isResponseClickBack = false;
		}
	}

	/**
	 * 设置是否支持下拉刷新
	 * @param flag
     */
	public void setPull2Refreshable(boolean flag) {
		setWebViewRefreshSupport(flag);
	}
	/**
	 * 
	 * 屏蔽顶部和底部点击事件
	 * @param isBlock true 屏蔽，false 取消屏蔽"
	 *  params：yintaimobile://ShieldTopBottom?state=true
	 */
	public void blockTopBottomBar(boolean isBlock) {
		/*if (getTitlelayout()!=null) {
			if (getTitlelayout().getLeftBtnIv()!=null) {
				H5CallNativeUtils.setViewEnabled(getTitlelayout().getLeftBtnIv(), !isBlock);
			}
			if (getTitlelayout().getRightBtnTv()!=null) {
				H5CallNativeUtils.setViewEnabled(getTitlelayout().getRightBtnTv(), !isBlock);
			}
		}
		if (getHomeTabLayout()!=null) {
			getHomeTabLayout().setAllViewEnabled(!isBlock);
		}*/
	}
	
	/**
	 * 标题栏设置
	 * params：yintaimobile://SetTitleBar?position=left&isShow=true&value=返回
	 * @param position 	String  left   right  middle
	 * @param isShow 	true 显示   false 隐藏
	 * @param value		String   标题栏的值
	 */
	public void setNativeTitleBar(String position, boolean isShow, String value) {
	/*	if ("left".equalsIgnoreCase(position)) {//左边返回不支持文字更改
			if (getTitlelayout()!=null) {
				getTitlelayout().setLeftBtnVisibility(isShow);
			}
		}else if ("middle".equalsIgnoreCase(position)){
			if (getTitlelayout()!=null&&getTitlelayout().getTitleTv()!=null) {
				getTitlelayout().getTitleTv().setVisibility(isShow? View.VISIBLE: View.GONE);
				if (!StringUtil.isBlank(value)) {
					getTitlelayout().setTitle(value);
				}
			}
		}else if ("right".equalsIgnoreCase(position)){
			if (getTitlelayout()!=null) {
				getTitlelayout().setRightBtnVisibility(isShow);
				if (isShow) {
					if (KEY_TITLEBAR_RIGHT_SHARED.equalsIgnoreCase(value)) {
						getTitlelayout().setRightBtn(null, R.mipmap.share_biao);
						getTitlelayout().getRightBtnTv().setTag(value);
					}else {
						getTitlelayout().setRightBtn(value, -1);
					}
				}
			}
		}*/
	}
	
/*	@Override
	public void onClickTitleBarLeftBtn() {
		if (isResponseClickBack&&mWebView!=null) {
    		H5ResponseClickBack();
		}else {
			super.onClickTitleBarLeftBtn();
		}
	}*/
	
	public void onClickTitleBarRightBtn(TextView rightBtnTv) {
		if (rightBtnTv!=null) {
			String str = rightBtnTv.getText().toString().trim();
			if (!StringUtil.isEmpty(str)) {
				mWebView.loadUrl("javascript:clickTitleBar('yintaimobile://ClickTitleBar?position=right&txtValue="+(str)+"')");
			}else {//右上角没有设置文字按钮，设置的展示图标
				String tag =null;
				try {
					tag = (String)rightBtnTv.getTag();
				} catch (Exception e) {
					tag =null;
				}
				if (!StringUtil.isEmpty(tag)) {
					if(tag.equalsIgnoreCase(KEY_TITLEBAR_RIGHT_SHARED)){//调用JS提供的分享方法
						mWebView.loadUrl("javascript:clickTitleBar('yintaimobile://ClickTitleBar?position=right&txtValue="+KEY_TITLEBAR_RIGHT_SHARED+"')");
						//mWebView.loadUrl("javascript:clickTitleBar()");//自己页面测试用
					}else {
						mWebView.loadUrl("javascript:clickTitleBar('yintaimobile://ClickTitleBar?position=right&txtValue="+tag+"')");
					}
				}
			}
		}
	}
	
	/**
	 * 启动第三方支付，其跳转类型为{ JumpType#PayNow}
	 * @param params
	 * 	其值类似： yintaimobile://PayNow?orderId=125544&orderSource=1<br>
	 *  orderSource: 1:电子小票支付、2:ERP订单支付 
	 */
	public void startPayNow(String params){
		this.payJumpUrl = StringUtil.f(params);
		// JumpCtrler.doJump(this, params);
	}
	
	/**
	 * JS支付结果通知H5 
	 * 支付结果：params：yintaimobile://PayResult?orerId=125544
	 * @param orderId 订单Id
	 */
	protected void payResult2H5(String orderId) {
		mWebView.loadUrl("javascript:operateCallback('yintaimobile://PayResult?orerId="+orderId+"')");
	}
	
	/** JS调用H5的返回处理机制 - 回传H5 标题栏触发对应事件 */
	public void H5ResponseClickBack() {
		mWebView.loadUrl("javascript:clickTitleBar('yintaimobile://ClickTitleBar?position=left&txtValue=返回')");
	}
	/** JS调用H5的返回处理机制 - 回传H5 扫描结果 */
	public void h5CallBack4Recode(String recode) {
		mWebView.loadUrl("javascript:operateCallback('yintaimobile://ScanResult?barcode="+recode+"')");
	}
	/**
	 * JS告诉H5登录结果
	 * 登陆结果：params: yintaimobile://LoginResult"
	 */
	protected void loginResult2H5() {
		mWebView.loadUrl("javascript:operateCallback('yintaimobile://LoginResult')");
		//新增逻辑处理
		try {
			webViewClient.callHandler("loginSuccess",
					H5CallNativeUtils.getuserinfo(BaseWebviewActivity.this),
					new WVJBWebViewClient.WVJBResponseCallback() {
						@Override
						public void callback(Object data) {
							L.d("WVJB",data.toString());
						}
					});
		} catch (Exception e) {
			L.e(e);
		}
	}
	/*@Override
	public void respForPay(int payType, boolean payResultStatus, String msg) {
		if(payResultStatus){
			String orderID = null ;
			JumpURI jumpURI = JumpCtrler.parseJumpParams(payJumpUrl);
			if( jumpURI != null && jumpURI.getParamMap() != null ){
				//获取订单信息
				orderID = jumpURI.getParamMap().get(EventDispatcher.KEY_PARAM_ORDERID);
			}
			payResult2H5(StringUtil.f(orderID));
			payJumpUrl = null ;
		}
	}*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*if (requestCode==LoginActivity.LOGIN_RESULT_CODE) {
			loginResult2H5();
		}
		if (requestCode== CaptureActivity.REQUESTCODE_CAPTURERESULT&&resultCode==RESULT_OK) {
			if(!IntentUtils.isEmpty(data)){
				String recode =IntentUtils.getBundleStr(data.getExtras(),CaptureActivity.KEY_INTENT_RESULTRECODE);
				if(StringUtil.isNotEmpty(recode)){
					h5CallBack4Recode(recode);
				}
			}
		}
		//刷新页面
		else if(BASE_WEBVIEW_RESULT_CODE==resultCode){
			if(null!=data){
				if(data.getBooleanExtra(ACTIVITY_FOR_RESULT_INTENT_KEY_REFRESH, false)){
					if(null!=mWebView&&!StringUtil.isBlank(mWebView.getUrl())){
						mWebView.reload();
					}
				}
			}
		}
		
		//选择/上传 图片/文件
		if (null != mUploadMessage){
			isDialogColse2ChoisePic=false;
			Uri uri = null;
			//拍照
			if(requestCode == REQ_CAMERA ){
				afterOpenCamera();
				uri = cameraUri;
			}
			//选择图片
			else if(requestCode == REQ_CHOOSE){
				uri = afterChosePic(data);
			}
			mUploadMessage.onReceiveValue(uri);
			mUploadMessage = null;
		}
		*/
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public String getPageType() {
		/*if(null!=mWebView&&!StringUtil.isBlank(mWebView.getUrl())){
			//结算中心
			if(mWebView.getUrl().contains(Constant.NETURL_DEAL_CENTER)){
				return "104816";
			}
		}
		return super.getPageType();*/
		return "";
	}
	public void dismissLoaddingView() {
		//添加下拉刷新后，下拉刷新的加载框架要和页面的加载同步
		if (mPullRefreshWebView!=null) {
			mPullRefreshWebView.onRefreshComplete();
		}
	}
	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public final boolean checkSDcard() {
		boolean flag = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!flag) {
			Toast.makeText(this, "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
	
	protected final void selectImage() {
		if (!checkSDcard())
			return;
		String[] selectPicTypeStr = { "相机","图库" };
		//
        showSelectionDialog("上传图片方式", selectPicTypeStr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
				// 相机拍摄
				case 0:
					isDialogColse2ChoisePic=true;
					openCamera();
					break;
				// 手机相册
				case 1:
					isDialogColse2ChoisePic=true;
					chosePic();
					break;
				default:
					break;
				}
				compressPath = Environment
						.getExternalStorageDirectory()
						.getPath()
						+ "/fuiou_wmp/temp";
				new File(compressPath).mkdirs();
				compressPath = compressPath + File.separator
						+ "compress.jpg";
            	menuDialog.dismiss();
            }
        });
        menuDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(null!=mUploadMessage){
					if(!isDialogColse2ChoisePic){
						mUploadMessage.onReceiveValue(null);
						mUploadMessage = null;
					}
				}
				
			}
		});
	}
	
	  /**
     * 选择链接地址对话框.
     * 
     * @param string 联网地址名称数组.
     * @param selections 联网地址名称内容.
     * @param checkedIndex 选中索引.
     * @param listener 点击监听.
     */
	private void showSelectionDialog(String string, String[] selections, int checkedIndex,
									 final DialogInterface.OnClickListener listener) {
        menuDialog = new YTUploadPicDialog.Builder(this).setTitle(string)
                .setSingleChoiceItems(selections, checkedIndex, listener).show();
    }
	
	/**
	 * 打开照相机
	 */
	private void openCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imagePaths = Environment.getExternalStorageDirectory().getPath()
				+ "/fuiou_wmp/temp/"
				+ (System.currentTimeMillis() + ".jpg");
		// 必须确保文件夹路径存在，否则拍照后无法完成回调
		File vFile = new File(imagePaths);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		cameraUri = Uri.fromFile(vFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(intent, REQ_CAMERA);
	}

	/**
	 * 拍照结束后
	 */
	private void afterOpenCamera() {
		File f = new File(imagePaths);
		if(null!=f&&f.exists()&&f.length()>0){
			addImageGallery(f);
			File newFile = FileUtils.compressFile(f.getPath(), compressPath);
			Uri.fromFile(newFile);
		}else{
			cameraUri=null;
		}
	}

	/** 解决拍照后在相册中找不到的问题 */
	private void addImageGallery(File file) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	/**
	 * 本地相册选择图片
	 */
	private void chosePic() {
		FileUtils.delFile(compressPath);
		SystemSupportUtils.callSysGallery(this, REQ_CHOOSE);
	}

	/**
	 * 选择照片后结束
	 * 
	 * @param data
	 */
	private Uri afterChosePic(Intent data) {
		if(null!=data&&null!=data.getData()){
			Uri selectedImage = data.getData();
			String picturePath = SystemSupportUtils.getSysImagePath(this, selectedImage);
			if(StringUtil.isNotEmpty(picturePath)){
				String temp=picturePath;
				temp=temp.toLowerCase();
				if(temp != null && (temp.endsWith(".png")||temp.endsWith(".jpg"))){
					File newFile = FileUtils.compressFile(picturePath, compressPath);
					return Uri.fromFile(newFile);
				}
				else{
					Toast.makeText(this, "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
			}
		}
		return null;
	}
	
	/**
	 * 自定义WebChromeClient内部类
	 * @author LangShaoPeng
	 *
	 */
	public class BaseWebChromeClient extends WebChromeClient {
		
		public void onProgressChanged(WebView view, int progress) {
			super.onProgressChanged(view, progress);
			L.d(TAG,"onProgressChanged progress = " + progress);
		}
		// 以上代码放在在Activity或则Fragment中的onCreate方法中
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {// 新版本的方法
			String message = consoleMessage.message();
			int lineNumber = consoleMessage.lineNumber();
			String sourceID = consoleMessage.sourceId();
			MessageLevel messageLevel = consoleMessage.messageLevel();
			return super.onConsoleMessage(consoleMessage);
		}

		// For Android 3.0+
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			if (mUploadMessage != null)
				return;
			mUploadMessage = uploadMsg;
			selectImage();
		}

		// For Android < 3.0
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			openFileChooser(uploadMsg, "");
		}

		// For Android > 4.1.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			openFileChooser(uploadMsg, acceptType);
		}
		
		//全屏播放视频
		@SuppressLint("NewApi")
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			//如果一个视图已经存在，那么立刻终止并新建一个
			if(mH5FullScreenVideoView != null){
				callback.onCustomViewHidden();
				return;
			}
			//设置幕横向
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //设置全屏
            //findViewById(R.id.frame).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            //隐藏Title布局
		/*	if(null!=mHeadLayout){
				mHeadLayout.setVisibility(View.GONE);
			}*/
			//显示全屏视频布局
			mFLH5FullScreenVideoView.setVisibility(View.VISIBLE);
			mFLH5FullScreenVideoView.setBackgroundColor(getResources().getColor(R.color.black));
			//将视频视图添加到全屏视频布局中
			mFLH5FullScreenVideoView.addView(view);
			//隐藏网页布局
			mFLWebView.setVisibility(View.GONE);
			
			mH5FullScreenVideoView = view;
			mWCCCustomViewCallBack = callback;
		}
		
		//结束全屏播放视频
		@SuppressLint("NewApi")
		@Override
		public void onHideCustomView() {
			if(mH5FullScreenVideoView == null){
				return;
			}
			//设置屏幕纵向
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //设置非全屏
            //findViewById(R.id.frame).setSystemUiVisibility(View.INVISIBLE);
            //显示Title布局
			/*if(null!=mHeadLayout){
				mHeadLayout.setVisibility(View.VISIBLE);
			}*/
			//显示网页布局
			mFLWebView.setVisibility(View.VISIBLE);
			//将视频视图从全屏视频布局中移除
			mFLH5FullScreenVideoView.removeView(mH5FullScreenVideoView);
			//隐藏全屏视频布局
			mFLH5FullScreenVideoView.setVisibility(View.GONE);
			mH5FullScreenVideoView = null;
			mWCCCustomViewCallBack.onCustomViewHidden();
		}
		
	}

}