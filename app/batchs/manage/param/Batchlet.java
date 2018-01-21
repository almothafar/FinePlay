package batchs.manage.param;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class Batchlet extends AbstractBatchlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private JobContext jobCtx;

	@Override
	public String process() throws Exception {

		final Properties jobParameters = BatchRuntime.getJobOperator().getJobExecution(jobCtx.getExecutionId()).getJobParameters();

		LOGGER.info("List :{}", jobParameters.get("param.list"));
		LOGGER.info("Item :{}", jobParameters.get("param.item"));

		return null;
	}
}
