package common.core.image;

import java.net.URI;

import javax.annotation.Nonnull;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultImage.class)
public interface Image {

	@Nonnull
	String getName();

	@Nonnull
	URI getLink();

	@Nonnull
	default String getInformation() {

		return "";
	}

	@Nonnull
	Credit getCredit();

	public static interface Credit {

		@Nonnull
		String getName();

		@Nonnull
		URI getLink();

		@Nonnull
		default String getInformation() {

			return "";
		}
	}
}
