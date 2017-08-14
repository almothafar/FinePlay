package common.core.mapping;

import org.mapstruct.factory.Mappers;

import com.google.inject.AbstractModule;

import models.framework.mapping.BeanMapper;

public class BeanMappingModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(BeanMapper.class).toInstance(Mappers.getMapper(BeanMapper.class));
	}
}
