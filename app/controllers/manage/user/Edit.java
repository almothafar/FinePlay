package controllers.manage.user;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.criteria.Root;
import javax.security.auth.login.AccountException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.data.validation.groups.Create;
import common.data.validation.groups.Delete;
import common.data.validation.groups.Update;
import common.system.MessageKeys;
import common.utils.DateTimes;
import controllers.user.UserService;
import models.base.EntityDao;
import models.manage.user.EditFormContent;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import models.user.User.Role;
import models.user.User.Theme;
import models.user.User_;
import play.api.PlayException;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.MessagesApi;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class Edit extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messages;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private FormFactory formFactory;

	@Inject
	private UserService userService;

	private final EntityDao<models.user.User> userDao = new EntityDao<models.user.User>() {
	};

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@Transactional()
	@RequireCSRFCheck
	public CompletionStage<Result> create() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> createForm = formFactory.form(EditFormContent.class, Create.class).bindFromRequest();

			if (!createForm.hasErrors()) {

				final EditFormContent createFormContent = createForm.get();
				if (createFormContent.getRoles() == null) {

					createFormContent.setRoles(Collections.emptyList());
				}

				final String userId = createFormContent.getUserId();
				final String password = createFormContent.getPassword();
				final String rePassword = createFormContent.getRePassword();
				final Set<Role> roles = createFormContent.getRoles().stream().filter(role -> role != null).collect(Collectors.toCollection(() -> EnumSet.noneOf(Role.class)));
				final Theme theme = Theme.DEFAULT;
				final Locale locale = Locale.US;
				final ZoneId zoneId = ZoneOffset.UTC;
				final Long companyId = createFormContent.getCompanyId();

				final models.user.User user;
				try {

					if (!password.equals(rePassword)) {

						throw new AccountException(messages.get(lang(), MessageKeys.SYSTEM_ERROR_PASSWORD_NOTEQUAL));
					}

					if (userService.isExist(manager, userId)) {

						throw new AccountException(messages.get(lang(), MessageKeys.SYSTEM_ERROR_USERID_EXIST));
					}

					user = new models.user.User();

					user.setUserId(userId);
					user.setPassword(password);
					user.setRoles(EnumSet.copyOf(roles));
					user.setTheme(theme);
					user.setLocale(locale);
					user.setZoneId(zoneId);
					if (Objects.nonNull(companyId)) {

						final models.company.Company company = manager.find(models.company.Company.class, companyId);
						user.setCompany(company);
					}
					user.setExpireDateTime(LocalDateTime.now().plusYears(1000));

					userService.create(manager, user);
				} catch (final AccountException e) {

					final Form<EditFormContent> failureCreateForm = formFactory.form(EditFormContent.class).fill(createFormContent);
					failureCreateForm.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureCreateForm);
				} catch (final Exception e) {

					final Form<EditFormContent> failureUpdateForm = formFactory.form(EditFormContent.class).fill(createFormContent);
					failureUpdateForm.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureUpdateForm);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureEdit(createForm);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@Transactional()
	@RequireCSRFCheck
	public CompletionStage<Result> update() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> updateForm = formFactory.form(EditFormContent.class, Update.class).bindFromRequest();

			if (!updateForm.hasErrors()) {

				final EditFormContent updateFormContent = updateForm.get();
				if (updateFormContent.getRoles() == null) {

					updateFormContent.setRoles(Collections.emptyList());
				}

				final String userId = updateFormContent.getUserId();
				final String newUserId = updateFormContent.getNewUserId();
				final Set<Role> roles = updateFormContent.getRoles().stream().filter(role -> role != null).collect(Collectors.toCollection(() -> EnumSet.noneOf(Role.class)));
				final Long companyId = updateFormContent.getCompanyId();
				final LocalDateTime updateDateTime = updateFormContent.getUpdateDateTime();

				final models.user.User user;
				try {

					final boolean isUpdateUserId = !userId.equals(newUserId);
					if (isUpdateUserId && userService.isExist(manager, newUserId)) {

						throw new AccountException(messages.get(lang(), MessageKeys.SYSTEM_ERROR_USERID_EXIST));
					}

					try {

						user = userDao.read(manager, models.user.User.class, (builder, query) -> {

							final LocalDateTime serverDateTime = DateTimes.getServerDateTime(updateDateTime);

							final Root<models.user.User> root = query.from(models.user.User.class);
							query.where(builder.and(//
									builder.equal(root.get(User_.userId), userId), //
									builder.equal(root.get(User_.updateDateTime), serverDateTime)));
						});
					} catch (final NoResultException e) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT), //
								e);
					}

					user.setUserId(newUserId);
					user.setRoles(EnumSet.copyOf(roles));
					if (Objects.nonNull(companyId)) {

						final models.company.Company company = manager.find(models.company.Company.class, companyId);
						user.setCompany(company);
					} else {

						user.setCompany(null);
					}
					user.setExpireDateTime(LocalDateTime.now().plusYears(1000));

					userService.update(manager, user);
				} catch (final AccountException e) {

					final Form<EditFormContent> failureUpdateForm = formFactory.form(EditFormContent.class).fill(updateFormContent);
					failureUpdateForm.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureUpdateForm);
				} catch (final Exception e) {

					final Form<EditFormContent> failureUpdateForm = formFactory.form(EditFormContent.class).fill(updateFormContent);
					failureUpdateForm.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureUpdateForm);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureEdit(updateForm);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	@Transactional()
	@RequireCSRFCheck
	public CompletionStage<Result> delete() {

		final Result result = jpaApi.withTransaction(manager -> {

			final Form<EditFormContent> deleteForm = formFactory.form(EditFormContent.class, Delete.class).bindFromRequest();

			if (!deleteForm.hasErrors()) {

				final EditFormContent deleteFormContent = deleteForm.get();

				final String userId = deleteFormContent.getUserId();
				final LocalDateTime updateDateTime = deleteFormContent.getUpdateDateTime();

				final models.user.User user;
				try {

					try {

						user = userDao.read(manager, models.user.User.class, (builder, query) -> {

							final LocalDateTime serverDateTime = DateTimes.getServerDateTime(updateDateTime);

							final Root<models.user.User> root = query.from(models.user.User.class);
							query.where(builder.and(//
									builder.equal(root.get(User_.userId), userId), //
									builder.equal(root.get(User_.updateDateTime), serverDateTime)));
						});
					} catch (final NoResultException e) {

						throw new PlayException(//
								messages.get(lang(), MessageKeys.CONSISTENT) + " " + messages.get(lang(), MessageKeys.ERROR), //
								messages.get(lang(), MessageKeys.SYSTEM_ERROR_STATE_NOTCONSISTENT), //
								e);
					}

					userService.delete(manager, user);
				} catch (final AccountException e) {

					final Form<EditFormContent> failureDeleteForm = formFactory.form(EditFormContent.class).fill(deleteFormContent);
					failureDeleteForm.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureDeleteForm);
				} catch (final Exception e) {

					final Form<EditFormContent> failureDeleteForm = formFactory.form(EditFormContent.class).fill(deleteFormContent);
					failureDeleteForm.withGlobalError(e.getLocalizedMessage());
					return failureEdit(failureDeleteForm);
				}

				final ObjectMapper mapper = new ObjectMapper();
				final ObjectNode response = mapper.createObjectNode();
				response.put("status", "success");

				return ok(response);
			} else {

				return failureEdit(deleteForm);
			}
		});

		return CompletableFuture.supplyAsync(() -> {

			return result;
		});
	}

	private Result failureEdit(final Form<EditFormContent> editForm) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();
		result.put("status", "error");

		final ArrayNode globalErrorsNode = mapper.createArrayNode();
		editForm.globalErrors().forEach(validationError -> {

			globalErrorsNode.add(messages.get(lang(), validationError.message()));
		});
		result.set("globalErrors", globalErrorsNode);

		final ObjectNode errorsNode = mapper.createObjectNode();
		editForm.allErrors().stream().forEach(error -> {

			final String property = error.key();

			final ArrayNode propertyErrorNode = mapper.createArrayNode();
			error.messages().forEach(message -> propertyErrorNode.add(messages.get(lang(), message)));

			errorsNode.set(property, propertyErrorNode);
		});
		result.set("errors", errorsNode);

		return badRequest(result);
	}
}
