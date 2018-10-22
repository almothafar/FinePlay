package models.system;

import common.system.SessionKeys;
import play.data.validation.Constraints;

public class SlideInfoFormContent {

	// Play
	@Constraints.Required
	private String url;

	// Play
	@Constraints.Required
	private String returnUrl;

	public static final String URL = "url";
	public static final String RETURNURL = SessionKeys.RETURN_URL;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the returnUrl
	 */
	public String getReturnUrl() {
		return returnUrl;
	}

	/**
	 * @param returnUrl the returnUrl to set
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
}
