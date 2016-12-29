package sooyu.webview.oauth;

public class OAuth2Client {

	private final String username;
	private final String password;
	private final String client_id;
	private final String client_secret;
	private final String site;
	private final String grant_type;
	
	public OAuth2Client(String grantType, String clientId, String clientSecret, String site) {
		this.username = "";
		this.password = "";
		this.grant_type=grantType;
		this.client_id = clientId;
		this.client_secret = clientSecret;
		this.site = site;
	}
	
	public OAuth2Client(String username, String password, String clientId, String clientSecret, String site) {
		this.grant_type="";
		this.username = username;
		this.password = password;
		this.client_id = clientId;
		this.client_secret = clientSecret;
		this.site = site;
	}

	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}


	public String getClientId() {
		return client_id;
	}


	public String getClientSecret() {
		return client_secret;
	}


	public String getSite() {
		return site;
	}
	
	public String getGrantType() {
		return grant_type;
	}

	public OAuthTokenResponse getAccessToken() {
		OAuth2Config oauthConfig = new OAuth2Config.OAuth2ConfigBuilder(username, password, client_id, client_secret, site)
			.grantType(grant_type).build();
		return OAuthUtils.getAccessToken(oauthConfig);
	}
}
