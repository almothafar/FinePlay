package common.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import common.utils.Sessions;
import models.system.System;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import models.user.User;
import models.user.User.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.jpa.Transactional;
import play.http.DefaultActionCreator;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Http.Headers;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Results;

public class ActionCreator extends DefaultActionCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActionCreator.class);

	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public Action createAction(Request request, Method actionMethod) {

		LOGGER.info("========== method={} uri={} remote-address={} ==========", request.method(), request.uri(), request.remoteAddress());
		LOGGER.info("{}#{}", actionMethod.getDeclaringClass().getName(), actionMethod.getName());

		final System.PermissionsAllowed permissionsAllowed = resolvePermissionsAllowed(actionMethod);

		return new Action.Simple() {

			@Override
			public CompletionStage<Result> call(Context ctx) {

				LOGGER.info(createArgsMessage(ctx.args));
				LOGGER.info(createRequestHeadersMessage(ctx.request().getHeaders()));
				LOGGER.info(createSessionMessage(ctx.session()));

				final String rolesValue = ctx.session().get(User.ROLES);
				final boolean isSignIn = rolesValue != null && !rolesValue.isEmpty();
				final CompletionStage<Result> stage;
				if (!isSignIn) {

					stage = callNotSignIn(ctx, permissionsAllowed);
				} else {

					final Set<Role> roles = Sessions.toRoles(rolesValue);
					stage = callSignIn(ctx, permissionsAllowed, roles);
				}

				LOGGER.info("========================================================");
				return stage;
			}

			private CompletionStage<Result> callNotSignIn(@Nonnull final Context ctx, @Nullable final PermissionsAllowed permissionsAllowed) {

				if (Objects.nonNull(permissionsAllowed)) {

					return createUnauthorizedPromise();
				}

				return delegate.call(ctx);
			}

			@SuppressWarnings("null")
			private CompletionStage<Result> callSignIn(@Nonnull final Context ctx, @Nullable final PermissionsAllowed permissionsAllowed, @Nonnull final Set<Role> roles) {

				if (Objects.nonNull(permissionsAllowed)) {

					final Set<Permission> workPermissions = getWorkPermissions(permissionsAllowed);
					LOGGER.info("WorkPermissions: {}", workPermissions);

					final Set<Permission> userPermissions = User.getPermissions(roles);
					LOGGER.info("User roles :{}, User permissions :{}", roles, userPermissions);

					if (!System.isPermissionAllowed(workPermissions, userPermissions)) {

						return createUnauthorizedPromise();
					}
				}

				return delegate.call(ctx);
			}
		};
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

	private static String createArgsMessage(final Map<String, Object> args) {

		final StringBuilder builder = new StringBuilder();

		builder.append("---------- Args").append("\n");

		final String argsContents = args.entrySet().stream().map(entry -> {

			final String key = StringUtils.leftPad(entry.getKey(), 25, ' ');
			final String value = entry.getValue().toString();

			return key + ": " + value;
		}).collect(Collectors.joining("\n"));
		builder.append(argsContents);

		return builder.toString();
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

		final String sessionContents = session.entrySet().stream().map(entry -> {

			final String key = StringUtils.leftPad(entry.getKey(), 25, ' ');
			final String value = entry.getValue().toString();

			return key + ": " + value;
		}).collect(Collectors.joining("\n"));
		builder.append(sessionContents);

		return builder.toString();
	}

	private CompletionStage<Result> createUnauthorizedPromise() {

		final Result unauthorized = Results.unauthorized(views.html.system.pages.unauthorized.render());
		return CompletableFuture.completedFuture(unauthorized);
	}

	private static Set<Permission> getWorkPermissions(final System.PermissionsAllowed permissionsAllowed) {

		final Set<Permission> workPermissions = EnumSet.copyOf(new HashSet<>(Arrays.asList(permissionsAllowed.value())));
		return workPermissions;
	}
}
