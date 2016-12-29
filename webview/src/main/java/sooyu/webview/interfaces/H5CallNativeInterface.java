package sooyu.webview.interfaces;

public interface H5CallNativeInterface {
	/**协议解析	-	解析协议规则JSON并做对应的处理*/
	public void h5_2_android(String protocolJson);
	/**协议解析	-	解析协议规则字符串并做对应的处理*/
	public void jumpProtocol(String protocolStr);
	
	/**WebView	-	加载指定URL网址*/
    public void loadUrl(String url);
    /**WebView	-	重新加载URL*/
    public void reloadUrl();
    /**WebView	-	WebView回退*/
    public void goBack();
    
    /**Activity	-	发送Message消息*/
    public void sendMessage(int msg, String json);
    /**Activity	-	发送Message消息并显示提示框*/
    public void showMsg(String msg);
    /**Activity	-	显示加载中的显示框*/
    public void showProgress(); 
    /**Activity	-	隐藏加载中的显示框*/
    public void disProgress();
    
    /**接收H5 页面BI数据  */
    public void onBiEvent(String eventJson);
}