package controllers.framework.beantype;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.inject.Inject;

import models.framework.beantype.Bean;
import models.framework.beantype.BeanFormContent;
import models.framework.beantype.BeanMapper;
import models.framework.beantype.BeanFormContent.Inner;
import models.framework.beantype.BeanMapper.CustomConvertorClass;
import models.framework.beantype.BeanMapper.CustomConvertorMethod;
import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;
import javax.annotation.Nonnull;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Http.Request;

@PermissionsAllowed
public class Mapping extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private BeanMapper mapper;

	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		mapping();
		return ok("mapped.");
	}

	public void mapping() {

		final BeanFormContent beanFormContent = new BeanFormContent();
		beanFormContent.setIgnore("ignore");
		beanFormContent.setValue("value");
		beanFormContent.setMapValue("mapValue");
		beanFormContent.setFormValue("formValue");
		beanFormContent.setIntValue(100);
		beanFormContent.setStringValue("1000");
		beanFormContent.setStrings(Arrays.asList("string0", "string1", "string2"));
		final Inner object = new Inner();
		object.setValue("innerValue");
		beanFormContent.setObject(object);
		beanFormContent.setDateValue(LocalDateTime.of(2001, 2, 3, 4, 5));
		beanFormContent.setNumberValue(1000);
		beanFormContent.setCustomValue("     customValue     ");

		final Bean bean = mapper.toBean(beanFormContent);

		System.out.println(bean.toString());
	}

	@CustomConvertorClass
	public static class CustomConvertor {

		@CustomConvertorMethod
		public String trim(@Nullable final String string) {

			return Objects.isNull(string) ? null : string.trim();
		}
	}
}
