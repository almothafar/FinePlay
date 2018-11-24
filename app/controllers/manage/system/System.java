package controllers.manage.system;

import java.lang.invoke.MethodHandles;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.Application;
import play.db.jpa.JPAApi;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;
import play.mvc.Security.Authenticated;

public class System extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private Provider<Application> applicationProvider;

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result shutdown(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		return jpaApi.withTransaction(manager -> {

			final play.api.Application application = applicationProvider.get().asScala();

			LOGGER.info("EntityManager#flush start");
			manager.flush();
			LOGGER.info("EntityManager#flush end");
			LOGGER.info("EntityManager#clear start");
			manager.clear();
			LOGGER.info("EntityManager#clear end");

			LOGGER.info("Application#stop start");
			application.stop();
			LOGGER.info("Application#stop end");

			return ok("Shutdown executed.");
		});
	}
}
