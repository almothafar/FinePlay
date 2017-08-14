package models.framework.mapping;

import java.util.List;

public class Bean {

	private String ignore;
	private String value;
	private String mapValue;
	private String dtoValue;
	private String stringValue;
	private int intValue;
	private List<String> strings;
	private Inner object;
	private String dateValue;
	private String numberValue;
	private String customValue;

	public static class Inner {

		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "Inner [value=" + value + "]";
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

	public String getDtoValue() {
		return dtoValue;
	}

	public void setDtoValue(String dtoValue) {
		this.dtoValue = dtoValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
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

	public String getDateValue() {
		return dateValue;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}

	public String getNumberValue() {
		return numberValue;
	}

	public void setNumberValue(String numberValue) {
		this.numberValue = numberValue;
	}

	public String getCustomValue() {
		return customValue;
	}

	public void setCustomValue(String customValue) {
		this.customValue = customValue;
	}

	@Override
	public String toString() {
		return "Bean [ignore=" + ignore + ", value=" + value + ", mapValue=" + mapValue + ", dtoValue=" + dtoValue + ", stringValue=" + stringValue + ", intValue=" + intValue + ", strings=" + strings + ", object=" + object + ", dateValue=" + dateValue + ", numberValue=" + numberValue + ", customValue=" + customValue + "]";
	}
}
