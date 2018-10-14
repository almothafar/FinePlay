package batchs.manage.fail;

import java.lang.invoke.MethodHandles;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import common.system.MessageKeys;
import play.i18n.Langs;
import play.i18n.MessagesApi;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

@Named
public class Batchlet extends AbstractBatchlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private Config config;

	@Inject
	private Langs langs;

	@Inject
	private MessagesApi messages;

	@Inject
	private MailerClient mailer;

	@Override
	public String process() throws Exception {

		final Exception exception = new IllegalStateException(messages.get(langs.availables().get(0), MessageKeys.FAILURE));

		LOGGER.info(exception.getLocalizedMessage());

		final Email email = new Email()//
				.setSubject(config.getString("system.url"))//
				.setFrom("Mister FROM <from@email.com>")//
				.addTo("Miss TO <to@email.com>")//
				.setBodyText(exception.getLocalizedMessage());
		mailer.send(email);

		throw exception;
	}
}
