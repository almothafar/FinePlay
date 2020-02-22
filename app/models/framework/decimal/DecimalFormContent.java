package models.framework.decimal;

import java.math.BigDecimal;

public class DecimalFormContent {

	private BigDecimal decimal;

	public static final String DECIMAL = "decimal";

	public String validate() {

		return null;
	}

	public BigDecimal getDecimal() {

		return decimal;
	}

	public void setDecimal(BigDecimal decimal) {

		this.decimal = decimal;
	}
}
