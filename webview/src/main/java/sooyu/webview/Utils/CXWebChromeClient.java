package sooyu.webview.Utils;

import android.content.Context;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import sooyu.webview.pulltorefresh.IPullToRefresh;

/**
 * 整合下拉刷新/选择文件上传 WebChromeClient
 * @author LangShaoPeng
 *
 */
public class CXWebChromeClient extends WebChromeClient {
	
	private Context mContext=null;
	
	/** 下拉刷新相关 */
	private IPullToRefresh<?> mIPullToRefresh=null;
	
	/** 文件选择/上传 */
	ValueCallback<Uri> mUploadMessage;
	
	public CXWebChromeClient(Context context, IPullToRefresh<?> iPullToRefresh){
		this.mContext=context;
		this.mIPullToRefresh=iPullToRefresh;
	}
	
	public void setIPullToRefresh(IPullToRefresh<?> iPullToRefresh){
		this.mIPullToRefresh=iPullToRefresh;
	}
	
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		if (newProgress == 100) {
			if(null!=mIPullToRefresh){
				mIPullToRefresh.onRefreshComplete();
			}
		}
	}
	
	// For Android 3.0+
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
		if (mUploadMessage != null)
			return;
		mUploadMessage = uploadMsg;
		WebViewUploadFileUtils.selectImage(mContext);
	}
	
	// For Android < 3.0
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		openFileChooser(uploadMsg, "");
	}
	
	// For Android > 4.1.1
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
		openFileChooser(uploadMsg, acceptType);
	}
	

}
