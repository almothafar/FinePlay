package controllers.manage.system;

import javax.inject.Inject;

import play.mvc.Controller;
import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.Logger;
import play.Logger.ALogger;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class System extends Controller {

	private static final ALogger LOGGER = Logger.of(System.class);

	@Inject
	private JPAApi jpaApi;

	@Transactional
	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = {Permission.MANAGE})
	public Result shutdown() {

		final play.api.Application application = common.system.System.getInjector().instanceOf(play.api.Application.class);

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
