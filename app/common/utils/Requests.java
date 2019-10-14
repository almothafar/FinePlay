package common.utils;

import java.lang.invoke.MethodHandles;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.jboss.weld.exceptions.IllegalStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.cache.SyncCacheApi;
import play.mvc.Http;
import play.mvc.Http.Request;

public class Requests {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Requests() {
	}

	public static boolean isFirstSubmit(@Nonnull final Request request, @Nonnull final SyncCacheApi syncCache) {

		final String previousSubmitToken;
		final String submitToken;
		synchronized (syncCache) {

			final Optional<String> previousSubmitTokenOptional = syncCache.get("previousSubmitToken");
			previousSubmitToken = previousSubmitTokenOptional.orElse(null);
			submitToken = request.queryString("csrfToken").orElse(null);
			syncCache.set("previousSubmitToken", submitToken, 10);
		}
		LOGGER.info(createSubmitMessage(previousSubmitToken, submitToken));

		if (Objects.isNull(previousSubmitToken)) {

			return true;
		}

		if (Objects.isNull(submitToken)) {

			throw new IllegalStateException("Not target of this method.");
		}

		if (previousSubmitToken.equals(submitToken)) {

			return false;
		}

		return true;
	}

	private static String createSubmitMessage(final String previousSubmitToken, final String submitToken) {

		final StringBuilder builder = new StringBuilder();

		builder.append("---------- Submit").append("\n");

		final List<SimpleEntry<String, String>> entries = new ArrayList<>();
		entries.add(new SimpleEntry<String, String>("Previous-Submit-Token", previousSubmitToken));
		entries.add(new SimpleEntry<String, String>("Submit-Token", submitToken));
		final String submitContents = entries.stream().map(entry -> {

			final String key = StringUtils.leftPad(entry.getKey(), 25, ' ');
			final String value = Objects.toString(entry.getValue());

			return key + ": " + value;
		}).collect(Collectors.joining("\n"));
		builder.append(submitContents);

		return builder.toString();
	}

	public static final boolean isAjax(@Nonnull final Request request) {

		// Call of jQuery only.
		final boolean isAJax = request.getHeaders().contains(Http.HeaderNames.X_REQUESTED_WITH) && "XMLHttpRequest".equals(request.getHeaders().get(Http.HeaderNames.X_REQUESTED_WITH).get());
		return isAJax;
	}
}
