package batchs.manage.db;

import java.util.Locale;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class Batchlet extends AbstractBatchlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(Batchlet.class);

	@Inject
	EntityManager em;

	@Override
	public String process() throws Exception {

		LOGGER.info("Entity manager: {}", em);

		em.getTransaction().begin();

		final models.user.User user = em.find(models.user.User.class, 1L);
		user.setLocale(Locale.JAPANESE);

		em.merge(user);

		em.getTransaction().commit();

		return null;
	}
}
