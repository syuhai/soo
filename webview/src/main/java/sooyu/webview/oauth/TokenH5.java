package sooyu.webview.oauth;

/**
 * H5调用本地存储的OAuth  AccessToken Bean
 * @author LangShaoPeng
 *
 */
public class TokenH5 {
	/** Token值 */
	private String access_token;
	/** Token类型 */
	private String token_type;
	/** Token生命周期 */
	private long expires_in;
	/** 获取Token的时间 */
	private long gettoken_time;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public long getGettoken_time() {
		return gettoken_time;
	}

	public void setGettoken_time(long gettoken_time) {
		this.gettoken_time = gettoken_time;
	}

}
