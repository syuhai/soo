package sooyu.webview.oauth;
/**
 * 
 * @author LangShaoPeng
 *
 */
public enum GatewayExceptionCode {
	L00001,//网关数据接口请求返回JSON字符串转JSON对象错误Code
	L00002,//获取Token，返回的Token正确JSON字符串中没有Token值Code
	L00003,//获取Token，返回的Token错误JSON字符串Code
	L00004,//获取Token，数据接口返回JSON字符串转JSON对象错误Code
	L00005,//获取Token错误、网关数据接口因为Token导致的错误   HTTP访问失败Code
	L00006,//网关数据接口请求错误默认Code
	L00007,//网关-服务器端未找到对应的服务异常Code
	L00008,//网关-服务器端未处理异常Code
	L00009,//网关-访问接口未被授权
	L00010;//网关-重新登录


	/**
	 * 判断给定的字符串枚举中是否存在
	 * @param str
	 * @return
	 */
	public static boolean have(String str){
		for(GatewayExceptionCode code : GatewayExceptionCode.values()){
           if(code.toString().equals(str)){
        	   return true;
           }
        }
		return false;
	}
}
