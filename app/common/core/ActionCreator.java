package common.core;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import common.utils.Sessions;
import models.system.System;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import models.user.User;
import models.user.User.Role;
import models.user.User_;
import play.db.jpa.JPAApi;
import play.http.DefaultActionCreator;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Action;
import play.mvc.Http.Headers;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Results;

public class ActionCreator extends DefaultActionCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@SuppressWarnings("rawtypes")
	@Override
	public Action createAction(Request request, Method actionMethod) {

		return jpaApi.withTransaction(manager -> {

			LOGGER.info("========== method={} uri={} remote-address={} ==========", request.method(), request.uri(), request.remoteAddress());
			LOGGER.info("{}#{}", actionMethod.getDeclaringClass().getName(), actionMethod.getName());

			final System.PermissionsAllowed permissionsAllowed = resolvePermissionsAllowed(actionMethod);

			return new Action.Simple() {

				@Override
				public CompletionStage<Result> call(Request req) {

					MDC.put(User_.USER_ID, req.session().getOptional(User_.USER_ID).orElse(null));

					LOGGER.info(createRequestHeadersMessage(req.getHeaders()));
					LOGGER.info(createSessionMessage(req.session()));

					final String rolesValue = req.session().getOptional(User_.ROLES).orElse(null);
					final boolean isSignIn = rolesValue != null && !rolesValue.isEmpty();
					final CompletionStage<Result> stage;
					if (!isSignIn) {

						stage = callNotSignIn(req, permissionsAllowed);
					} else {

						final Set<Role> roles = Sessions.toRoles(rolesValue);
						stage = callSignIn(req, permissionsAllowed, roles);
					}

					LOGGER.info("========================================================");
					MDC.remove(User_.USER_ID);
					return stage;
				}

				private CompletionStage<Result> callNotSignIn(@Nonnull final Request req, @Nullable final PermissionsAllowed permissionsAllowed) {

					if (Objects.nonNull(permissionsAllowed)) {

						return createUnauthorizedPromise(req);
					}

					return delegate.call(req);
				}

				private CompletionStage<Result> callSignIn(@Nonnull final Request req, @Nullable final PermissionsAllowed permissionsAllowed, @Nonnull final Set<Role> roles) {

					if (Objects.nonNull(permissionsAllowed)) {

						final Set<Permission> workPermissions = getWorkPermissions(permissionsAllowed);
						LOGGER.info("WorkPermissions: {}", workPermissions);

						final Set<Permission> userPermissions = User.getPermissions(roles);
						LOGGER.info("User roles :{}, User permissions :{}", roles, userPermissions);

						if (!System.isPermissionAllowed(workPermissions, userPermissions)) {

							return createUnauthorizedPromise(req);
						}
					}

					return delegate.call(req);
				}
			};
		});
	}

	private static System.PermissionsAllowed resolvePermissionsAllowed(@Nonnull final Method actionMethod) {

		String permissionType = null;
		System.PermissionsAllowed permissionsAllowed = actionMethod.getAnnotation(System.PermissionsAllowed.class);
		if (permissionsAllowed == null) {

			final Class<?> controllerClass = actionMethod.getDeclaringClass();
			permissionsAllowed = controllerClass.getAnnotation(System.PermissionsAllowed.class);

			if (permissionsAllowed == null) {

				permissionType = "NONE";
			} else {

				permissionType = "CLASS(Controller)";
			}
		} else {

			permissionType = "METHOD(Controller method)";
		}

		LOGGER.info("Permission type: {}", permissionType);
		return permissionsAllowed;
	}

	private static String createRequestHeadersMessage(final Headers headers) {

		final StringBuilder builder = new StringBuilder();

		builder.append("---------- Request headers").append("\n");

		final String headersContents = headers.toMap().entrySet().stream().map(entry -> {

			final String key = StringUtils.leftPad(entry.getKey(), 25, ' ');
			final String value = entry.getValue().stream().collect(Collectors.joining("\n", "[ ", " ]"));

			return key + ": " + value;
		}).collect(Collectors.joining("\n"));
		builder.append(headersContents);

		return builder.toString();
	}

	private static String createSessionMessage(final Session session) {

		final StringBuilder builder = new StringBuilder();

		builder.append("---------- Session").append("\n");

		final String sessionContents = session.data().entrySet().stream().map(entry -> {

			final String key = StringUtils.leftPad(entry.getKey(), 25, ' ');
			final String value = entry.getValue().toString();

			return key + ": " + value;
		}).collect(Collectors.joining("\n"));
		builder.append(sessionContents);

		return builder.toString();
	}

	private CompletionStage<Result> createUnauthorizedPromise(final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final Result unauthorized = Results.unauthorized(views.html.system.pages.unauthorized.render(request, lang, messages));
		return CompletableFuture.completedFuture(unauthorized);
	}

	private static Set<Permission> getWorkPermissions(final System.PermissionsAllowed permissionsAllowed) {

		final Set<Permission> workPermissions = EnumSet.copyOf(new HashSet<>(Arrays.asList(permissionsAllowed.value())));
		return workPermissions;
	}
}
