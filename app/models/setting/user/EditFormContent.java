package models.setting.user;

import common.data.validation.groups.Update;
import play.data.validation.Constraints;

public class EditFormContent {

	// Play
	@Constraints.Required(groups = {Update.class})
	private String theme;

	// Play
	@Constraints.Required(groups = {Update.class})
	private String locale;

	// Play
	@Constraints.Required(groups = {Update.class})
	private String zoneId;

	public String getTheme() {

		return theme;
	}

	public void setTheme(String theme) {

		this.theme = theme;
	}

	public String getLocale() {

		return locale;
	}

	public void setLocale(String locale) {

		this.locale = locale;
	}

	public String getZoneId() {

		return zoneId;
	}

	public void setZoneId(String zoneId) {

		this.zoneId = zoneId;
	}
}
