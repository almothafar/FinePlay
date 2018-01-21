package batchs.manage.slow;

import java.lang.invoke.MethodHandles;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class Batchlet extends AbstractBatchlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public String process() throws Exception {

		for (int i = 0; i < 30; i++) {

			LOGGER.info("zzz...");
			Thread.sleep(1000);
		}

		LOGGER.info("wake up!");
		return null;
	}
}
