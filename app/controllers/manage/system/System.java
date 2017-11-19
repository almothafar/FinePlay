package controllers.manage.system;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.Application;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class System extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(System.class);

	@Inject
	private Provider<Application> applicationProvider;

	@Inject
	private JPAApi jpaApi;

	@Transactional
	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result shutdown() {

		final play.api.Application application = applicationProvider.get().asScala();

		LOGGER.info("EntityManager#flush start");
		jpaApi.em().flush();
		LOGGER.info("EntityManager#flush end");
		LOGGER.info("EntityManager#clear start");
		jpaApi.em().clear();
		LOGGER.info("EntityManager#clear end");

		LOGGER.info("Application#stop start");
		application.stop();
		LOGGER.info("Application#stop end");

		return ok("Shutdown executed.");
	}
}
