package common.utils;

import java.util.Objects;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

public class Exceptions {

	public static <RESULT> RESULT callQuietly(@Nonnull final Callable<RESULT> checkedProcess) {

		Objects.nonNull(checkedProcess);

		try {

			return checkedProcess.call();
		} catch (Exception e) {

			throw new IllegalStateException("The occurrence of an exception is not assumed.", e);
		}
	}
}
