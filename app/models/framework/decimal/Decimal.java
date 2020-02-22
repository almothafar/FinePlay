package models.framework.decimal;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "DECIMALS", //
		uniqueConstraints = { @UniqueConstraint(columnNames = { Decimal_.USER__ID }) }, //
		indexes = { @Index(columnList = Decimal_.USER__ID) })
public class Decimal {

	@Id
	@Column(nullable = false)
	// Play
	private long user_Id;

	@Column(precision = 100, scale = 2) // 12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678.90
	private BigDecimal decimal;

	public static final String USER_ID = "user_Id";
	public static final String DECIMAL = "decimal";

	public long getUser_Id() {

		return user_Id;
	}

	public void setUser_Id(long user_Id) {

		this.user_Id = user_Id;
	}

	public BigDecimal getDecimal() {

		return decimal;
	}

	public void setDecimal(BigDecimal decimal) {

		this.decimal = decimal;
	}
}
