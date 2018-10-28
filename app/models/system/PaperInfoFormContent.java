package models.system;

import common.system.SessionKeys;
import play.data.validation.Constraints;

public class PaperInfoFormContent {

	// Play
	@Constraints.Required
	private String url;

	// Play
	@Constraints.Required
	private String returnUrl;

	// Play
	@Constraints.Required
	private String size;

	private boolean pageNo;

	private boolean print;

	public static final String URL = "url";
	public static final String RETURNURL = SessionKeys.RETURN_URL;
	public static final String SIZE = "size";
	public static final String PAGENO = "pageNo";
	public static final String PRINT = "print";

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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public boolean isPageNo() {
		return pageNo;
	}

	public void setPageNo(boolean pageNo) {
		this.pageNo = pageNo;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}
}
