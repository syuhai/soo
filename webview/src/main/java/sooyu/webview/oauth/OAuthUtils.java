package sooyu.webview.oauth;


import android.util.Base64;

import com.yintai.tools.Constant;
import com.yintai.tools.GatewayUtil;
import com.yintai.tools.SharedPreferencesTools;
import com.yintai.tools.StringUtil;
import com.yintai.tools.YTLog;
import com.yintai.tools.net.DataServer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class OAuthUtils {
	/** Token */
	public static final String TOKEN_SAVE="TOKEN_SAVE";
	/** Token Type*/
	public static final String TOKEN_SAVE_TYPE="TOKEN_SAVE_TYPE";
	/** Token 保存到本地时间 */
	public static final String TOKEN_SAVE_TIME="TOKEN_SAVE_TIME";
	/** Token 过期周期 */
	public static final String TOKEN_SAVE_EXPIRES_IN="TOKEN_SAVE_EXPIRES_IN";
	/** Token 过期时间 */
	public static final String TOKEN_OVERDUE_TIME="TOKEN_OVERDUE_TIME";
	/** 本地存储Token是否更新过 */
	public static final String TOKEN_IS_UPDATE="TOKEN_IS_UPDATE";

	public static String getProtectedResource(OAuth2Client client, Token token, String path) {
		
		String resourceURL = client.getSite() + path;
		HttpGet get = new HttpGet(resourceURL);
		get.addHeader(OAuthConstants.AUTHORIZATION,
				getAuthorizationHeaderForAccessToken(token
						.getAccessToken()));
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		String responseString = "";
		int code = -1;
		try {
			response = httpClient.execute(get);
			code = response.getStatusLine().getStatusCode();
			if (code >= 400) {
				throw new RuntimeException(
								"Could not access protected resource. Server returned http code: "
										+ code);
					}

			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
//			System.out.println(responseString);
			//handleResponse(response);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				response.getEntity().consumeContent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return responseString;
	}

	public static OAuthTokenResponse getAccessToken(OAuth2Config oauthDetails) {
		HttpPost post = new HttpPost(oauthDetails.getTokenEndPointUrl());
		String clientId = oauthDetails.getClientId();
		String clientSecret = oauthDetails.getClientSecret();
		String scope = oauthDetails.getScope();

		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
		parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE,
				oauthDetails.getGrantType()));
		parametersBody.add(new BasicNameValuePair(OAuthConstants.USERNAME,
				oauthDetails.getUsername()));
		parametersBody.add(new BasicNameValuePair(OAuthConstants.PASSWORD,
				oauthDetails.getPassword()));

		if (isValid(clientId)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,
					clientId));
		}
		if (isValid(clientSecret)) {
			parametersBody.add(new BasicNameValuePair(
					OAuthConstants.CLIENT_SECRET, clientSecret));
		}
		if (isValid(scope)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE,
					scope));
		}
		HttpClient client= getSSLHttpClient();

		//DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		Token accessToken = null;
		TokenError accessTokenError=new TokenError();
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				JSONObject oauthResponse = null;
				String content=EntityUtils.toString(response.getEntity());
				YTLog.d("token...."+ content);
				oauthResponse = new JSONObject(content);
				//返回数据是正确的Token数据， 数据是否正确返回
				if(oauthResponse.has("access_token")&&!isBlank(oauthResponse.getString("access_token"))&&
						oauthResponse.has("token_type")&&!isBlank(oauthResponse.getString("token_type"))){
					accessToken=new Token(oauthResponse.getLong("expires_in"),
							oauthResponse.getString("token_type"), 
							"", oauthResponse.getString("access_token"));
					//保存Token
					saveToken(accessToken);
					return accessToken;
				}
				//返回数据是正确的Token数据，access_token字段对应的值为空
				else if(oauthResponse.has("access_token")&&isBlank(oauthResponse.getString("access_token"))){
					accessTokenError.setErrorDescription(GatewayUtil.getExceptionMsg(GatewayExceptionCode.L00002.toString()));
					return accessTokenError;
				}
			}
			else {
				accessTokenError.setErrorDescription(GatewayUtil.getExceptionMsg(GatewayExceptionCode.L00003.toString()));
			}
		} catch (JSONException e1) {
			accessTokenError.setErrorDescription(GatewayUtil.getExceptionMsg(GatewayExceptionCode.L00004.toString()));
			e1.printStackTrace();
		} catch (ParseException e) {
			accessTokenError.setErrorDescription(GatewayUtil.getExceptionMsg(GatewayExceptionCode.L00005.toString()));
			e.printStackTrace();
		} catch (IOException e) {
			accessTokenError.setErrorDescription(GatewayUtil.getExceptionMsg(GatewayExceptionCode.L00005.toString()));
			e.printStackTrace();
		} 
		return accessTokenError;
	}

	
	public static Token refreshAccessToken(Token token, OAuth2Config oauthDetails) {
		HttpPost post = new HttpPost(oauthDetails.getTokenEndPointUrl());
		String clientId = oauthDetails.getClientId();
		String clientSecret = oauthDetails.getClientSecret();

		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
		parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE,
				"refresh_token"));
		parametersBody.add(new BasicNameValuePair(OAuthConstants.REFRESH_TOKEN,
				token.getRefreshToken()));
		

		if (isValid(clientId)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,
					clientId));
		}
		if (isValid(clientSecret)) {
			parametersBody.add(new BasicNameValuePair(
					OAuthConstants.CLIENT_SECRET, clientSecret));
		}

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		Token accessToken = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

			response = client.execute(post);
//			System.out.println(response.getStatusLine().getStatusCode());
			int code = response.getStatusLine().getStatusCode();
				if (code >= 400) {
//					System.out.println("Retry with client credentials");
					post.removeHeaders(OAuthConstants.AUTHORIZATION);
					post.addHeader(
							OAuthConstants.AUTHORIZATION,
							getBasicAuthorizationHeader(
									oauthDetails.getClientId(),
									oauthDetails.getClientSecret()));

					try {
						response.getEntity().consumeContent();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	

					
					response = client.execute(post);
					code = response.getStatusLine().getStatusCode();
					if (code >= 400) {
						throw new RuntimeException(
								"Could not retrieve access token for user: "
										+ oauthDetails.getUsername());
					}
				}

				//Map<String, Object> map = handleResponse(response);
				//accessToken = new Token(new Long((Integer) map.get(OAuthConstants.EXPIRES_IN)), (String) map.get(OAuthConstants.TOKEN_TYPE), (String) map.get(OAuthConstants.REFRESH_TOKEN), (String) map.get(OAuthConstants.ACCESS_TOKEN));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return accessToken;
	}

	public static String getAuthorizationHeaderForAccessToken(String accessToken) {
		return OAuthConstants.BEARER + " " + accessToken;
	}

	public static String getBasicAuthorizationHeader(String username,
													 String password) {
		return OAuthConstants.BASIC + " "
				+ encodeCredentials(username, password);
	}

	public static String encodeCredentials(String username, String password) {
		String cred = username + ":" + password;
		String encodedValue = null;
		byte[] encodedBytes = Base64.encode(cred.getBytes(), Base64.NO_WRAP);
		encodedValue = new String(encodedBytes);
//		System.out.println("encodedBytes " + new String(encodedBytes));

		byte[] decodedBytes = Base64.decode(encodedBytes, Base64.NO_WRAP);
//		System.out.println("decodedBytes " + new String(decodedBytes));

		return encodedValue;

	}

	public static boolean isValid(String str) {
		return (str != null && str.trim().length() > 0);
	}
	
	/**
	 * 
	 * @return
	 */
	public static HttpClient getSSLHttpClient() { 
	   try { 
	       KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	       trustStore.load(null, null); 

	       SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
	       sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	       HttpParams params = new BasicHttpParams();
	       HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1); 
	       HttpProtocolParams.setContentCharset(params, HTTP.UTF_8); 

	       SchemeRegistry registry = new SchemeRegistry(); 
	       registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80)); 
	       registry.register(new Scheme("https", sf, 443)); 

	       ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry); 
	       return new DefaultHttpClient(ccm, params); 
	   } catch (Exception e) {
	       return new DefaultHttpClient(); 
	   } 
	} 
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		if(null==str)
    	{
    		return true;
    	}
    	str=str.trim();
    	if(0==str.length())
    	{
    		return true;
    	}
        return false;
	}
	
	/**
	 * 保存Token
	 * @param token
	 */
	public synchronized static void saveToken(Token token){
		if(null!=token){
			//Token保存的时间
			long saveTokenTimeMillis= System.currentTimeMillis();
			//保存Token值
			SharedPreferencesTools.getInstance(DataServer.getApplication()).putString(TOKEN_SAVE, token.getAccessToken());
			//保存Token Type
			SharedPreferencesTools.getInstance(DataServer.getApplication()).putString(TOKEN_SAVE_TYPE, token.getTokenType());
			//保存Token保存时间
			SharedPreferencesTools.getInstance(DataServer.getApplication()).putLong(TOKEN_SAVE_TIME, saveTokenTimeMillis);
			//保存Token过期周期
			SharedPreferencesTools.getInstance(DataServer.getApplication()).putLong(TOKEN_SAVE_EXPIRES_IN, token.getExpiresIn());
			//保存Token过期时间
			long saveTokenExpiresTimeMillis=(token.getExpiresIn() * 1000L) + saveTokenTimeMillis;
			SharedPreferencesTools.getInstance(DataServer.getApplication()).putLong(TOKEN_OVERDUE_TIME, saveTokenExpiresTimeMillis);
			//设置Token状态为已更新
			updateLocalSaveToken(true);
		}
	}
	
	/**
	 * 获取本地存储的AccessToken
	 * @return
	 */
	public static String getLocalSaveToken(){
		return SharedPreferencesTools.getInstance(DataServer.getApplication()).getString(TOKEN_SAVE);
	}
	/**
	 * 获取本地存储的AccessTokenType
	 * @return
	 */
	public static String getLocalSaveTokenType(){
		return SharedPreferencesTools.getInstance(DataServer.getApplication()).getString(TOKEN_SAVE_TYPE);
	}
	
	/**
	 * 获取本地存储的AccessToken 保存时间
	 * @return
	 */
	public static long getLocalSaveTokenTime(){
		return SharedPreferencesTools.getInstance(DataServer.getApplication()).getLong(TOKEN_SAVE_TIME);
	}
	
	/**
	 * 获取本地存储的AccessToken 过期周期
	 * @return
	 */
	public static long getLocalSaveTokenExpiresIn(){
		return SharedPreferencesTools.getInstance(DataServer.getApplication()).getLong(TOKEN_SAVE_EXPIRES_IN);
	}
	/**
	 * 获取本地存储的AccessToken 过期时间
	 * @return
	 */
	public static long getLocalSaveTokenOverdueTime(){
		return SharedPreferencesTools.getInstance(DataServer.getApplication()).getLong(TOKEN_OVERDUE_TIME);
	}
	
	/**
	 * 更新token存储状态是否是最新<br>
	 * true:  <br>
	 * GatewayRequest<br>
	 * -Header<br>
	 * --(Service-Context-App)<br>
	 * ---trace  包含traceId及其他数据<br><br>
	 * false:<br>
	 * GatewayRequest<br>
	 * -Header<br>
	 * --(Service-Context-App)<br>
	 * ---trace 只包含traceId<br>
	 * @param isUpdate
	 */
	public static void updateLocalSaveToken(boolean isUpdate){
		SharedPreferencesTools.getInstance(DataServer.getApplication()).putBoolean(TOKEN_IS_UPDATE, isUpdate);
	}
	
	/**
	 * 获取Toke存储状态
	 * @return
	 */
	public static boolean localSaveTokenIsUpdated(){
		return SharedPreferencesTools.getInstance(DataServer.getApplication()).getBoolean(TOKEN_IS_UPDATE);
	}
	
	/**
	 * 本地是否有可用的Token<br>
	 * 本地有Token，并且Token不过期。<br>
	 * 不过期是基于本地的时间不会被修改的前提下。
	 * @return
	 */
	public static boolean localHaveAvailableToken(){
		//判断本地是否有保存Token，如果有保存的Token，Token是否过期。
		if(StringUtil.isBlank(OAuthUtils.getLocalSaveToken())||
				(getLocalSaveTokenOverdueTime()- System.currentTimeMillis()<=(1000L*60L))){
			return false;
		}
		return true;
	}
	
	/**
	 * 获取Token
	 * @return
	 */
	public static OAuthTokenResponse getAccessToken(){
		//获取Token
		OAuth2Client auth2Client=new OAuth2Client(OAuthConstants.OAUTH_GRANT_TYPE,
				OAuthConstants.OAUTH_CLINET_ID,OAuthConstants.OAUTH_CLIENT_SECRET, Constant.NETURL_OAUTH);
		return auth2Client.getAccessToken();
	}

}
