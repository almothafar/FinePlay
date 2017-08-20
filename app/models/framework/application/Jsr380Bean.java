package models.framework.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import common.system.MessageKeys;

public class Jsr380Bean {

	@AssertFalse(message = MessageKeys.JAVA_ERROR_ASSERTFALSE)
	private boolean java_constraint_AssertFalse = true;

	@AssertTrue(message = MessageKeys.JAVA_ERROR_ASSERTTRUE)
	private boolean java_constraint_AssertTrue = false;

	@Digits(message = MessageKeys.JAVA_ERROR_DIGITS, fraction = 7, integer = 3)
	private BigDecimal java_constraint_Digits = new BigDecimal("87654321.1234");

	@DecimalMax(message = MessageKeys.JAVA_ERROR_DECIMALMAX, value = "7")
	private int java_constraint_DecimalMax = 8;

	@DecimalMax(message = MessageKeys.JAVA_ERROR_DECIMALMAX, value = "8", inclusive = false)
	private int java_constraint_DecimalMax_inclusiveFalse = 8;

	@DecimalMin(message = MessageKeys.JAVA_ERROR_DECIMALMIN, value = "3")
	private int java_constraint_DecimalMin = 2;

	@DecimalMin(message = MessageKeys.JAVA_ERROR_DECIMALMIN, value = "2", inclusive = false)
	private int java_constraint_DecimalMin_inclusiveFalse = 2;

	@Future(message = MessageKeys.JAVA_ERROR_FUTURE)
	private LocalDateTime java_constraint_Future = LocalDateTime.MIN;

	@Past(message = MessageKeys.JAVA_ERROR_PAST)
	private LocalDateTime java_constraint_Past = LocalDateTime.MAX;

	@Null(message = MessageKeys.JAVA_ERROR_NULL)
	private String java_constraint_Null = "";

	@Size(message = MessageKeys.JAVA_ERROR_SIZE, min = 2, max = 8)
	private List<Object> java_constraint_Size = new ArrayList<>();

	@FutureOrPresent(message = MessageKeys.JAVA_ERROR_FUTUREORPRESENT)
	private LocalDateTime java_constraint_FutureOrPresent = LocalDateTime.MIN;

	@PastOrPresent(message = MessageKeys.JAVA_ERROR_PASTORPRESENT)
	private LocalDateTime java_constraint_PastOrPresent = LocalDateTime.MAX;

	@Negative(message = MessageKeys.JAVA_ERROR_NEGATIVE)
	private int java_constraint_Negative = Integer.MAX_VALUE;

	@NegativeOrZero(message = MessageKeys.JAVA_ERROR_NEGATIVEORZERO)
	private int java_constraint_NegativeOrZero = Integer.MAX_VALUE;

	@NotBlank(message = MessageKeys.JAVA_ERROR_NOTBLANK)
	private String java_constraint_NotBlank = " ";

	@NotEmpty(message = MessageKeys.JAVA_ERROR_NOTEMPTY)
	private List<String> java_constraint_NotEmpty = Collections.emptyList();

	@Positive(message = MessageKeys.JAVA_ERROR_POSITIVE)
	private int java_constraint_Positive = Integer.MIN_VALUE;

	@PositiveOrZero(message = MessageKeys.JAVA_ERROR_POSITIVEORZERO)
	private int java_constraint_PositiveOrZero = Integer.MIN_VALUE;

	//

	@NotNull(message = MessageKeys.JAVA_ERROR_NOTNULL)
	private String java_constraint_NotNull = null;

	@Max(message = MessageKeys.JAVA_ERROR_MAX, value = 7L)
	private int java_constraint_Max = 8;

	@Min(message = MessageKeys.JAVA_ERROR_MIN, value = 3L)
	private int java_constraint_Min = 2;

	@Pattern(message = MessageKeys.JAVA_ERROR_PATTERN, regexp = "(>_<)")
	private String java_constraint_Pattern = "@";

	@Email(message = MessageKeys.JAVA_ERROR_EMAIL)
	private String java_constraint_Email = "@";

	public boolean isJava_constraint_AssertFalse() {
		return java_constraint_AssertFalse;
	}

	public boolean isJava_constraint_AssertTrue() {
		return java_constraint_AssertTrue;
	}

	public BigDecimal getJava_constraint_Digits() {
		return java_constraint_Digits;
	}

	public int getJava_constraint_DecimalMax() {
		return java_constraint_DecimalMax;
	}

	public int getJava_constraint_DecimalMax_inclusiveFalse() {
		return java_constraint_DecimalMax_inclusiveFalse;
	}

	public int getJava_constraint_DecimalMin() {
		return java_constraint_DecimalMin;
	}

	public int getJava_constraint_DecimalMin_inclusiveFalse() {
		return java_constraint_DecimalMin_inclusiveFalse;
	}

	public LocalDateTime getJava_constraint_Future() {
		return java_constraint_Future;
	}

	public LocalDateTime getJava_constraint_Past() {
		return java_constraint_Past;
	}

	public String getJava_constraint_Null() {
		return java_constraint_Null;
	}

	public List<Object> getJava_constraint_Size() {
		return java_constraint_Size;
	}

	public LocalDateTime getJava_constraint_FutureOrPresent() {
		return java_constraint_FutureOrPresent;
	}

	public LocalDateTime getJava_constraint_PastOrPresent() {
		return java_constraint_PastOrPresent;
	}

	public int getJava_constraint_Negative() {
		return java_constraint_Negative;
	}

	public int getJava_constraint_NegativeOrZero() {
		return java_constraint_NegativeOrZero;
	}

	public String getJava_constraint_NotBlank() {
		return java_constraint_NotBlank;
	}

	public List<String> getJava_constraint_NotEmpty() {
		return java_constraint_NotEmpty;
	}

	public int getJava_constraint_Positive() {
		return java_constraint_Positive;
	}

	public int getJava_constraint_PositiveOrZero() {
		return java_constraint_PositiveOrZero;
	}

	public String getJava_constraint_NotNull() {
		return java_constraint_NotNull;
	}

	public int getJava_constraint_Max() {
		return java_constraint_Max;
	}

	public int getJava_constraint_Min() {
		return java_constraint_Min;
	}

	public String getJava_constraint_Pattern() {
		return java_constraint_Pattern;
	}

	public String getJava_constraint_Email() {
		return java_constraint_Email;
	}
}
