package common.core.batch;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

import javax.inject.Singleton;
import javax.transaction.TransactionManager;

import org.jberet.repository.JobRepository;
import org.jberet.se.BatchSEEnvironment;
import org.jberet.spi.ArtifactFactory;
import org.jberet.spi.JobTask;
import org.jberet.spi.JobXmlResolver;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Singleton
public class BatchEnvironment implements org.jberet.spi.BatchEnvironment {

	private final org.jberet.spi.BatchEnvironment batchSEEnvironment;

	public BatchEnvironment() {

		this.batchSEEnvironment = new BatchSEEnvironment();

		final Properties properties = this.batchSEEnvironment.getBatchConfigurationProperties();
		overrideProperties(properties);
	}

	private void overrideProperties(final Properties configProperties) {

		final Config config = ConfigFactory.load();
		final String batchConf = config.hasPath("system.batch.conf") ? config.getString("system.batch.conf") : BatchSEEnvironment.CONFIG_FILE_NAME;
		if (!BatchSEEnvironment.CONFIG_FILE_NAME.equals(batchConf)) {

			try (final InputStream configStream = getClassLoader().getResourceAsStream(batchConf)) {

				configProperties.load(configStream);
			} catch (final IOException e) {

				throw new UncheckedIOException("system.batch.conf: " + batchConf, e);
			}
		}
	}

	@Override
	public ClassLoader getClassLoader() {

		final ClassLoader classLoader = BatchEnvironment.class.getClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader);
		return classLoader;
	}

	@Override
	public ArtifactFactory getArtifactFactory() {

		return new common.core.batch.ArtifactFactory();
	}

	@Override
	public void submitTask(JobTask task) {

		batchSEEnvironment.submitTask(task);
	}

	@Override
	public TransactionManager getTransactionManager() {

		return batchSEEnvironment.getTransactionManager();
	}

	@Override
	public JobRepository getJobRepository() {

		return batchSEEnvironment.getJobRepository();
	}

	@Override
	public JobXmlResolver getJobXmlResolver() {

		return batchSEEnvironment.getJobXmlResolver();
	}

	@Override
	public Properties getBatchConfigurationProperties() {

		return batchSEEnvironment.getBatchConfigurationProperties();
	}
}
