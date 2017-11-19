package batchs.manage.hello.chunk;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Processor implements ItemProcessor {

	@Inject
	private JobContext jobCtx;

	@Override
	public Object processItem(Object item) throws Exception {

		final String message = jobCtx.getProperties().getProperty("message");
		System.out.println(message);

		final String line = (String) item;
		final StringBuilder builder = new StringBuilder();
		builder.append("Processor processItem : ");
		builder.append(line);
		final String returnValue = builder.toString();

		System.out.println(returnValue);
		return returnValue;
	}
}
