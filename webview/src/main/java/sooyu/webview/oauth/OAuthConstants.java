package sooyu.webview.oauth;


public class OAuthConstants {
	
	public static final String ACCESS_TOKEN = "access_token";
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_SECRET = "client_secret";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String AUTHENTICATION_SERVER_URL = "authentication_server_url";
	public static final String RESOURCE_SERVER_URL = "resource_server_url";
	public static final String GRANT_TYPE = "grant_type";
	public static final String SCOPE = "scope";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	public static final String BASIC = "Basic";
	public static final String JSON_CONTENT = "application/json";
	public static final String XML_CONTENT = "application/xml";
	public static final String URL_ENCODED_CONTENT = "application/x-www-form-urlencoded";
	public static final String EXPIRES_IN = "expires_in";
	public static final String TOKEN_TYPE = "token_type";
	public static final int HTTP_OK = 200;
	public static final int HTTP_FORBIDDEN = 403;
	public static final int HTTP_UNAUTHORIZED = 401;
	
	public static final String OAUTH_GRANT_TYPE = "client_credentials";
	
	public static String OAUTH_CLINET_ID = "54b95f95-95a8-4af6-8618-6f3ef9fd78cb";
	public static String OAUTH_CLIENT_SECRET = "NaGeD97UtqjO";
	
	
	/** 网关-服务未找到异常 -name*/
	public static String ERROR_GATEWAY_SERVICE_NOT_FOUND_EXCEPTION_NAME="org.nofdev.servicefacade.ServiceNotFoundException";
	/** 网关-未处理异常 -name */
	public static String ERROR_GATEWAY_UN_HANDLED_EXCEPTION_NAME="org.nofdev.servicefacade.UnhandledException";
	/** 网关-重新登录 -name */
	public static String ERROR_GATEWAY_RELOGIN_EXCEPTION_NAME="org.nofdev.exception.AuthenticationException";
	/** 网关-接口未授权-name */
	public static String ERROR_GATEWAY_UNAUTH_EXCEPTION_NAME="org.nofdev.exception.AuthorizationException";

	/** 服务器正在维护中 */
	public static final String GATEWAY_EXCEPTION_MSG="服务器正在维护中";
	/** 中文 括号左半边 */
	public static final String BRACKET_LEFT="（";
	/** 中文 括号右半边 */
	public static final String BRACKET_RIGHT="）";
	
	/** 网管数据接口请求Header Authorization TOKEN_TYPE bearer*/
	public static final String REQUEST_TOKEN_TYPE_BEARER="Bearer";
	/** 网管数据接口请求Header Authorization TOKEN_TYPE mac*/
	public static final String REQUEST_TOKEN_TYPE_MAC="MAC";
	
}
