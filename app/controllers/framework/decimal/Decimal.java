package controllers.framework.decimal;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.security.auth.login.AccountException;

import common.system.MessageKeys;
import controllers.user.UserService;
import models.framework.decimal.DecimalFormContent;
import models.system.System.PermissionsAllowed;
import models.user.User;
import models.user.User_;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Decimal extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	@Inject
	private UserService userService;

	@Authenticated(common.core.Authenticator.class)
	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final models.framework.decimal.Decimal readedDecimal = readDecimal(manager, request, messages);
			final DecimalFormContent readedDecimalFormContent = setDecimalFormContentValue(request, readedDecimal);

			final Form<DecimalFormContent> readedDecimalForm = formFactory.form(DecimalFormContent.class).fill(readedDecimalFormContent);

			final Map<String, Object> map = new LinkedHashMap<>();
			if (Objects.nonNull(readedDecimal.getDecimal())) {

				map.put("-", readedDecimal.getDecimal());
				map.put("万", readedDecimal.getDecimal().movePointLeft(4));
				map.put("億", readedDecimal.getDecimal().movePointLeft(8));
				map.put("兆", readedDecimal.getDecimal().movePointLeft(12));
			}

			return ok(views.html.framework.decimal.decimal.render(new HashMap<>(), readedDecimalForm, map, request, lang, messages));
		});
	}

	@Nonnull
	private models.framework.decimal.Decimal readDecimal(@Nonnull final EntityManager manager, @Nonnull final Request request, @Nonnull Messages messages) {

		final User user;
		try {

			user = userService.read(manager, messages, request.session().get(User_.USER_ID).get());
		} catch (final AccountException e) {

			throw new RuntimeException(e);
		}

		models.framework.decimal.Decimal decimal = manager.find(models.framework.decimal.Decimal.class, user.getId());
		final boolean isExist = Objects.nonNull(decimal);

		if (!isExist) {

			decimal = new models.framework.decimal.Decimal();
			decimal.setUser_Id(user.getId());
		}

		return decimal;
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result update(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final Form<DecimalFormContent> decimalForm = formFactory.form(DecimalFormContent.class).bindFromRequest(request);
			if (!decimalForm.hasErrors()) {

				final DecimalFormContent decimalFormContent = decimalForm.get();

				final models.framework.decimal.Decimal updatedDecimal;
				try {

					updatedDecimal = updateDecimal(manager, decimalFormContent, request, messages);
				} catch (IllegalStateException e) {

					final Map<String, String> alertInfo = new HashMap<String, String>() {
						{
							put("decimalWarning", "<strong>" + messages.at(MessageKeys.WARNING) + "</strong> " + e.getLocalizedMessage());
						}
					};

					return failureRead(alertInfo, decimalForm, new HashMap<>(), request, lang, messages);
				}
				final DecimalFormContent updatedDecimalFormContent = setDecimalFormContentValue(request, updatedDecimal);

				final Form<DecimalFormContent> updatedDecimalForm = formFactory.form(DecimalFormContent.class).fill(updatedDecimalFormContent);

				final Map<String, Object> map = new LinkedHashMap<>();
				if (Objects.nonNull(updatedDecimal.getDecimal())) {

					map.put("-", updatedDecimal.getDecimal());
					map.put("万", updatedDecimal.getDecimal().movePointLeft(4));
					map.put("億", updatedDecimal.getDecimal().movePointLeft(8));
					map.put("兆", updatedDecimal.getDecimal().movePointLeft(12));
				}

				return ok(views.html.framework.decimal.decimal.render(new HashMap<>(), updatedDecimalForm, map, request, lang, messages));
			} else {

				return failureRead(new HashMap<>(), decimalForm, new HashMap<>(), request, lang, messages);
			}
		});
	}

	@Nonnull
	private models.framework.decimal.Decimal updateDecimal(@Nonnull final EntityManager manager, @Nonnull final DecimalFormContent decimalFormContent, @Nonnull final Request request, @Nonnull final Messages messages) {

		final BigDecimal decimalValue = decimalFormContent.getDecimal();

		final User user;
		try {

			user = userService.read(manager, messages, request.session().get(User_.USER_ID).get());
		} catch (final AccountException e) {

			throw new RuntimeException(e);
		}

		models.framework.decimal.Decimal decimal = manager.find(models.framework.decimal.Decimal.class, user.getId());
		final boolean isExist = Objects.nonNull(decimal);

		if (!isExist) {

			decimal = new models.framework.decimal.Decimal();
			decimal.setUser_Id(user.getId());
		}

		decimal.setDecimal(decimalValue);

		if (!isExist) {

			manager.persist(decimal);
		} else {

			manager.merge(decimal);
		}

		return decimal;
	}

	@Nonnull
	private DecimalFormContent setDecimalFormContentValue(@Nonnull final Request request, @Nonnull final models.framework.decimal.Decimal decimal) {

		Objects.requireNonNull(decimal);

		final DecimalFormContent decimalFormContent = new DecimalFormContent();

		decimalFormContent.setDecimal(decimal.getDecimal());

		return decimalFormContent;
	}

	@Nonnull
	private Result failureRead(@Nonnull final Map<String, String> alertInfo, @Nonnull final Form<DecimalFormContent> decimalForm, @Nonnull final Map<String, Object> map, final Request request, final Lang lang, final Messages messages) {

		return badRequest(views.html.framework.decimal.decimal.render(alertInfo, decimalForm, map, request, lang, messages));
	}
}
