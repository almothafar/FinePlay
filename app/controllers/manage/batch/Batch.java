package controllers.manage.batch;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.system.System.Permission;
import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

public class Batch extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Authenticated(common.core.Authenticator.class)
	@PermissionsAllowed(value = { Permission.MANAGE })
	public Result index() {

		LOGGER.warn("");

		final JobOperator jobOperator = BatchRuntime.getJobOperator();

		final Path batchJobsPath = Paths.get(".", "conf", "META-INF", "batch-jobs");
		final List<String> jobNames;
		try {

			jobNames = Files.list(batchJobsPath).filter(path -> {

				final String fileName = path.getFileName().toString();
				return fileName.endsWith("xml");
			}).map(batchJobPath -> batchJobPath.getFileName().toString().replaceFirst("\\.xml$", "")).collect(Collectors.toList());
		} catch (IOException e) {

			throw new UncheckedIOException(e);
		}

		final List<JobExecution> jobExecutions = toJobExecutions(jobOperator, jobOperator.getJobNames());

		return ok(views.html.manage.batch.index.render(jobNames, jobExecutions));
	}

	private static List<JobExecution> toJobExecutions(@Nonnull final JobOperator jobOperator, @Nonnull final Set<String> jobNames) {

		return jobNames.stream()//
				.flatMap(jobName -> jobOperator.getJobInstances(jobName, 0, 1000).stream())//
				.flatMap(jobInstance -> jobOperator.getJobExecutions(jobInstance).stream())//
				.sorted((JobExecution j0, JobExecution j1) -> j0.getStartTime().compareTo(j1.getStartTime()))//
				.collect(Collectors.toList());
	}
}
