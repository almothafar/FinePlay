package controllers.framework.mapping;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.inject.Inject;

import models.framework.mapping.Bean;
import models.framework.mapping.BeanFormContent;
import models.framework.mapping.BeanFormContent.Inner;
import models.framework.mapping.BeanMapper;
import models.framework.mapping.BeanMapper.CustomConvertorClass;
import models.framework.mapping.BeanMapper.CustomConvertorMethod;
import models.system.System.PermissionsAllowed;
import play.mvc.Controller;
import play.mvc.Result;

@PermissionsAllowed
public class Mapping extends Controller {

	@Inject
	private BeanMapper mapper;

	public Result index() {

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
