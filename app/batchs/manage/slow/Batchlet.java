package batchs.manage.slow;

import java.lang.invoke.MethodHandles;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class Batchlet extends AbstractBatchlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private boolean running = true;

	@Override
	public String process() throws Exception {

		for (int i = 0; i < 30; i++) {

			if (!isRunning()) {

				LOGGER.info("jump out of bed.");
				return null;
			}

			LOGGER.info("zzz...");
			Thread.sleep(1000);
		}

		LOGGER.info("wake up!");
		return null;
	}

	@Override
	public void stop() throws Exception {

		setRunning(false);
	}

	public boolean isRunning() {

		return running;
	}

	public void setRunning(boolean running) {

		this.running = running;
	}
}
