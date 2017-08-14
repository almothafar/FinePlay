package models.framework.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import common.system.MessageKeys;
import play.data.validation.Constraints.Max;

public class Jsr303Bean {

	@AssertFalse(message = MessageKeys.CONSTRAINTS_ASSERTFALSE)
	private final boolean assertFalse = true;

	@AssertTrue(message = MessageKeys.CONSTRAINTS_ASSERTTRUE)
	private final boolean assertTrue = false;

	@Digits(message = MessageKeys.CONSTRAINTS_DIGITS, fraction = 7, integer = 3)
	private final BigDecimal digits = new BigDecimal("87654321.1234");

	@DecimalMax(message = MessageKeys.CONSTRAINTS_DECIMALMAX, value = "7")
	private final BigDecimal decimalMax = new BigDecimal(8);

	@DecimalMin(message = MessageKeys.CONSTRAINTS_DECIMALMIN, value = "3")
	private final BigDecimal decimalMin = new BigDecimal(2);

	@Future(message = MessageKeys.CONSTRAINTS_FUTURE)
	private final Date future = new Date(0);

	@Past(message = MessageKeys.CONSTRAINTS_PAST)
	private final Date past = new Date(Long.MAX_VALUE);

	@Null(message = MessageKeys.CONSTRAINTS_NULL)
	private final String nullValue = "";

	@Size(message = MessageKeys.CONSTRAINTS_SIZE, min = 2, max = 8)
	private final List<Object> size = new ArrayList<>();

	//

	@NotNull(message = MessageKeys.CONSTRAINTS_NOTNULL)
	private final String notNull = null;

	@Max(message = MessageKeys.CONSTRAINTS_MAX, value = 7L)
	private final int max = 8;

	@Min(message = MessageKeys.CONSTRAINTS_MIN, value = 3L)
	private final int min = 2;

	@Pattern(message = MessageKeys.CONSTRAINTS_PATTERN, regexp = "..")
	private final String pattern = "@";
}
