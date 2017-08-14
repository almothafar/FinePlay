package models.framework.mapping;

import java.time.LocalDateTime;
import java.util.List;

public class BeanFormContent {

	private String ignore;
	private String value;
	private String mapValue;
	private String formValue;
	private int intValue;
	private String stringValue;
	private List<String> strings;
	private Inner object;
	private LocalDateTime dateValue;
	private int numberValue;
	private String customValue;

	public static class Inner {

		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMapValue() {
		return mapValue;
	}

	public void setMapValue(String mapValue) {
		this.mapValue = mapValue;
	}

	public String getFormValue() {
		return formValue;
	}

	public void setFormValue(String formValue) {
		this.formValue = formValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public List<String> getStrings() {
		return strings;
	}

	public void setStrings(List<String> strings) {
		this.strings = strings;
	}

	public Inner getObject() {
		return object;
	}

	public void setObject(Inner object) {
		this.object = object;
	}

	public LocalDateTime getDateValue() {
		return dateValue;
	}

	public void setDateValue(LocalDateTime dateValue) {
		this.dateValue = dateValue;
	}

	public int getNumberValue() {
		return numberValue;
	}

	public void setNumberValue(int numberValue) {
		this.numberValue = numberValue;
	}

	public String getCustomValue() {
		return customValue;
	}

	public void setCustomValue(String customValue) {
		this.customValue = customValue;
	}
}
