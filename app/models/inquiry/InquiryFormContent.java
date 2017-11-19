package models.inquiry;

import common.data.validation.Constraints.UserId;
import common.data.validation.groups.Create;
import play.data.validation.Constraints;

public class InquiryFormContent {

	// Play
	@Constraints.Required(groups = { Create.class })
	// System
	@UserId
	private String userId;

	private String name;

	// Play
	@Constraints.Required(groups = { Create.class })
	private String type;

	// Play
	@Constraints.Required(groups = { Create.class })
	private String title;

	// Play
	@Constraints.Required(groups = { Create.class })
	private String content;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
