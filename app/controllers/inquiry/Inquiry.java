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
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;

public class Inquiry extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private SyncCacheApi syncCache;

	@Inject
	private FormFactory formFactory;

	@Inject
	private JPAApi jpa;

	private final EntityDao<models.inquiry.Inquiry> inquiryDao = new EntityDao<models.inquiry.Inquiry>() {
	};

	public Result index() {

		final InquiryFormContent inquiryFormContent = new InquiryFormContent();
		// inquiryFormContent.setType(Type.OTHER.name());
		final Form<InquiryFormContent> inquiryForm = formFactory.form(InquiryFormContent.class).fill(inquiryFormContent);

		return ok(views.html.inquiry.inquiry.render(inquiryForm));
	}

	@RequireCSRFCheck
	public Result send() {

		return jpa.withTransaction(manager -> {

			final Form<InquiryFormContent> inquiryForm = formFactory.form(InquiryFormContent.class, Create.class).bindFromRequest();

			if (!Requests.isFirstSubmit(request(), syncCache)) {

				LOGGER.info("Not first submit.");
				return ok(views.html.inquiry.send.complete.render(inquiryForm));
			}

			if (!inquiryForm.hasErrors()) {

				final InquiryFormContent inquiryFormContent = inquiryForm.get();

				final String userId = inquiryFormContent.getUserId();
				final String name = inquiryFormContent.getName();

				final Type type = Type.valueOf(inquiryFormContent.getType());
				final String title = inquiryFormContent.getTitle();
				final String content = inquiryFormContent.getContent();

				final models.inquiry.Inquiry inquiry;

				inquiry = new models.inquiry.Inquiry();
				inquiry.setLocale(lang().toLocale());
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

				return ok(views.html.inquiry.send.complete.render(inquiryForm));
			} else {

				return failureInquiry(inquiryForm);
			}
		});
	}

	private Result failureInquiry(final Form<InquiryFormContent> inquiryForm) {

		return badRequest(views.html.inquiry.inquiry.render(inquiryForm));
	}
}
