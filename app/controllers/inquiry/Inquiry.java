package controllers.inquiry;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.data.validation.groups.Create;
import common.utils.Requests;
import models.base.EntityDao;
import models.inquiry.Inquiry.Type;
import models.inquiry.InquiryFormContent;
import play.api.PlayException;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;

public class Inquiry extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private SyncCacheApi syncCache;

	@Inject
	private FormFactory formFactory;

	@Inject
	private JPAApi jpaApi;

	private final EntityDao<models.inquiry.Inquiry> inquiryDao = new EntityDao<models.inquiry.Inquiry>() {
	};

	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		final InquiryFormContent inquiryFormContent = new InquiryFormContent();
		// inquiryFormContent.setType(Type.OTHER.name());
		final Form<InquiryFormContent> inquiryForm = formFactory.form(InquiryFormContent.class).fill(inquiryFormContent);

		return ok(views.html.inquiry.inquiry.render(inquiryForm, request, lang, messages));
	}

	@RequireCSRFCheck
	public Result send(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		final Lang lang = messages.lang();

		return jpaApi.withTransaction(manager -> {

			final Form<InquiryFormContent> inquiryForm = formFactory.form(InquiryFormContent.class, Create.class).bindFromRequest(request);

			if (!inquiryForm.hasErrors()) {

				final boolean isFirstSubmit = Requests.isFirstSubmit(request, syncCache);

				final InquiryFormContent inquiryFormContent = inquiryForm.get();

				final String userId = inquiryFormContent.getUserId();
				final String name = inquiryFormContent.getName();

				final Type type = Type.valueOf(inquiryFormContent.getType());
				final String title = inquiryFormContent.getTitle();
				final String content = inquiryFormContent.getContent();

				final models.inquiry.Inquiry inquiry;

				inquiry = new models.inquiry.Inquiry();
				inquiry.setLocale(lang.toLocale());
				inquiry.setUserId(userId);
				inquiry.setName(name);
				inquiry.setType(type);
				inquiry.setTitle(title);
				inquiry.setContent(content);
				inquiry.setDateTime(LocalDateTime.now());

				try {

					inquiryDao.create(manager, inquiry);
				} catch (final EntityExistsException e) {

					throw new RuntimeException(e);
				}

				if (isFirstSubmit) {

					LOGGER.info("First submit.");
				} else {

					LOGGER.warn("Not first submit.");
					manager.getTransaction().setRollbackOnly();
				}

				return ok(views.html.inquiry.send.complete.render(inquiryForm, request, lang, messages));
			} else {

				return failureInquiry(inquiryForm, request, lang, messages);
			}
		});
	}

	private Result failureInquiry(final Form<InquiryFormContent> inquiryForm, final Request request, final Lang lang, final Messages messages) {

		return badRequest(views.html.inquiry.inquiry.render(inquiryForm, request, lang, messages));
	}
}
