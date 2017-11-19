package models.framework.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Qualifier;

import controllers.framework.mapping.Mapping.CustomConvertor;

@Mapper(uses = { CustomConvertor.class })
public interface BeanMapper {

	@Mappings({ //
			@Mapping(target = "ignore", ignore = true), //
			// value
			@Mapping(source = "mapValue", target = "mapValue"), //
			@Mapping(source = "formValue", target = "dtoValue"), //
			@Mapping(source = "intValue", target = "stringValue"), //
			@Mapping(source = "stringValue", target = "intValue"), //
			@Mapping(source = "strings", target = "strings"), //
			@Mapping(source = "object.value", target = "object.value"), //
			@Mapping(source = "dateValue", target = "dateValue", dateFormat = "yyyy/MM/dd"), //
			@Mapping(source = "numberValue", target = "numberValue", numberFormat = "#,###"), //
			@Mapping(target = "customValue", source = "customValue", qualifiedBy = { CustomConvertorClass.class, CustomConvertorMethod.class }) //
	})
	Bean toBean(BeanFormContent beanFormContent);

	@Qualifier
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.CLASS)
	public @interface CustomConvertorClass {
	}

	@Qualifier
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.CLASS)
	public @interface CustomConvertorMethod {
	}
}
