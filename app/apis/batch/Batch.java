package apis.batch;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.batch.operations.JobOperator;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import common.data.validation.Constraints.UserIdValidator;
import models.system.System.PermissionsAllowed;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Batch extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Batch.class);

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result start(@Nonnull final String jobName, @Nonnull final String userId) {

		LOGGER.warn("The author is short of knowledge of Batch.");

		final String decodedUserId = decodeUserId(userId);
		LOGGER.info("Batch#start jobName={} userId={}", jobName, decodedUserId);

		if (!new UserIdValidator().isValid(decodedUserId)) {

			return ok(createErrorResult(UserIdValidator.message));
		}

		final Properties jobParameters = new Properties();
		jobParameters.putAll(ctx().request().queryString());
		jobParameters.remove("csrfToken");

		final JobOperator jobOperator = BatchRuntime.getJobOperator();
		final long executionId = jobOperator.start(jobName, jobParameters);

		return ok(createStartResult(jobName, executionId));
	}

	private JsonNode createStartResult(@Nonnull final String jobName, final long executionId) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("jobName", jobName);
		result.put("executionId", executionId);

		return result;
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result stop(final long executionId, @Nonnull final String userId) {

		LOGGER.warn("The author is short of knowledge of Batch.");

		final String decodedUserId = decodeUserId(userId);
		LOGGER.info("Batch#stop executionId={} userId={}", executionId, decodedUserId);

		if (!new UserIdValidator().isValid(decodedUserId)) {

			return ok(createErrorResult(UserIdValidator.message));
		}

		final JobOperator jobOperator = BatchRuntime.getJobOperator();
		jobOperator.stop(executionId);

		return ok(createStopResult(executionId));
	}

	private JsonNode createStopResult(final long beforeExecutionId) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("beforeExecutionId", beforeExecutionId);

		return result;
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result restart(final long executionId, @Nonnull final String userId) {

		LOGGER.warn("The author is short of knowledge of Batch.");

		final String decodedUserId = decodeUserId(userId);
		LOGGER.info("Batch#restart executionId={} userId={}", executionId, decodedUserId);

		if (!new UserIdValidator().isValid(decodedUserId)) {

			return ok(createErrorResult(UserIdValidator.message));
		}

		final Properties jobParameters = new Properties();
		jobParameters.putAll(ctx().request().queryString());
		jobParameters.remove("csrfToken");

		final JobOperator jobOperator = BatchRuntime.getJobOperator();
		final long newExecutionId = jobOperator.restart(executionId, jobParameters);

		return ok(createReStartResult(executionId, newExecutionId));
	}

	private JsonNode createReStartResult(final long beforeExecutionId, final long executionId) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("beforeExecutionId", beforeExecutionId);
		result.put("executionId", executionId);

		return result;
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result abandon(final long executionId, @Nonnull final String userId) {

		LOGGER.warn("The author is short of knowledge of Batch.");

		final String decodedUserId = decodeUserId(userId);
		LOGGER.info("Batch#abandon executionId={} userId={}", executionId, decodedUserId);

		if (!new UserIdValidator().isValid(decodedUserId)) {

			return ok(createErrorResult(UserIdValidator.message));
		}

		final JobOperator jobOperator = BatchRuntime.getJobOperator();
		jobOperator.abandon(executionId);

		return ok(createAbandonResult(executionId));
	}

	private JsonNode createAbandonResult(final long beforeExecutionId) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("beforeExecutionId", beforeExecutionId);

		return result;
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result jobExecution(final long executionId, @Nonnull final String userId) {

		LOGGER.warn("The author is short of knowledge of Batch.");

		final String decodedUserId = decodeUserId(userId);
		LOGGER.info("Batch#jobexecution executionId={} userId={}", executionId, decodedUserId);

		if (!new UserIdValidator().isValid(decodedUserId)) {

			return ok(createErrorResult(UserIdValidator.message));
		}

		final JobOperator jobOperator = BatchRuntime.getJobOperator();
		JobExecution jobExecution = null;
		try {

			jobExecution = jobOperator.getJobExecution(executionId);
		} catch (NoSuchJobExecutionException e) {

			return ok(createErrorResult(e.getLocalizedMessage()));
		}

		return ok(createJobExecutionResult(executionId, jobExecution));
	}

	private JsonNode createJobExecutionResult(final long executionId, final JobExecution jobExecution) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("executionId", executionId);
		final ObjectNode jobExecutionNode = result.putObject("jobExecution");
		jobExecutionNode.put("batchStatus", jobExecution.getBatchStatus().name());
		jobExecutionNode.put("exitStatus", jobExecution.getExitStatus());

		return result;
	}

	private JsonNode createErrorResult(@Nonnull final String errorMessage) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("error", errorMessage);

		return result;
	}

	private static String decodeUserId(@Nonnull final String userId) {

		final String decodedUserId;
		try {

			decodedUserId = URLDecoder.decode(userId, StandardCharsets.UTF_8.name());
		} catch (final UnsupportedEncodingException e) {

			throw new RuntimeException(e);
		}

		return decodedUserId;
	}
}
