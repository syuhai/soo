package sooyu.webview.oauth;

/**
 * Oauth Token获取失败
 * 
 * @author LangShaoPeng
 *
 */
public class TokenError extends OAuthTokenResponse{
	private String error;
	private String error_description;
	private String error_uri;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorDescription() {
		return error_description;
	}

	public void setErrorDescription(String error_description) {
		this.error_description = error_description;
	}

	public String getErrorUri() {
		return error_uri;
	}

	public void setErrorUri(String error_uri) {
		this.error_uri = error_uri;
	}

}
