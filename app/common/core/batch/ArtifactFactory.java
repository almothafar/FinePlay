package common.core.batch;

import java.lang.reflect.Field;
import java.util.Objects;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.jberet.creation.AbstractArtifactFactory;
import org.jberet.creation.ArtifactFactoryWrapper;
import org.jberet.se.SEArtifactFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.i18n.Langs;
import play.i18n.MessagesApi;
import play.libs.mailer.MailerClient;

public class ArtifactFactory extends AbstractArtifactFactory {

	private final org.jberet.spi.ArtifactFactory artifactFactoryWrapper;

	public ArtifactFactory() {

		this.artifactFactoryWrapper = new ArtifactFactoryWrapper(new SEArtifactFactory());
	}

	@Override
	public Object create(String ref, Class<?> cls, ClassLoader classLoader) throws Exception {

		final Object artifact = artifactFactoryWrapper.create(ref, cls, classLoader);

		if (Objects.nonNull(artifact)) {

			doInjection(artifact);
		}

		return artifact;
	}

	@Override
	public void destroy(Object instance) {

		artifactFactoryWrapper.destroy(instance);
	}

	@Override
	public Class<?> getArtifactClass(String ref, ClassLoader classLoader) {

		return artifactFactoryWrapper.getArtifactClass(ref, classLoader);
	}

	private void doInjection(final Object obj) throws Exception {

		Class<?> clazz = obj.getClass();
		while (clazz != Object.class && !clazz.getPackage().getName().startsWith("javax.batch")) {

			for (final Field field : clazz.getDeclaredFields()) {

				if (!field.isSynthetic()) {

					Object fieldVal = null;
					if (Objects.nonNull(field.getAnnotation(Inject.class))) {

						final Class<?> fieldType = field.getType();
						if (fieldType == Config.class) {

							final Config config = ConfigFactory.load();
							fieldVal = config;
						} else if (fieldType == Langs.class) {

							final Langs langs = common.system.System.getInjector().instanceOf(Langs.class);
							fieldVal = langs;
						} else if (fieldType == MessagesApi.class) {

							final MessagesApi messages = common.system.System.getInjector().instanceOf(MessagesApi.class);
							fieldVal = messages;
						} else if (fieldType == MailerClient.class) {

							final MailerClient mailer = common.system.System.getInjector().instanceOf(MailerClient.class);
							fieldVal = mailer;
						} else if (fieldType == EntityManager.class) {

							final Config config = ConfigFactory.load();
							final String persistenceUnitName = config.hasPath("jpa.default") ? config.getString("jpa.default") : "defaultPersistenceUnit";
							final EntityManager em = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
							fieldVal = em;
						}
						if (Objects.nonNull(fieldVal)) {

							if (!field.canAccess(obj)) {

								field.setAccessible(true);
							}
							field.set(obj, fieldVal);
						}
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}
}
