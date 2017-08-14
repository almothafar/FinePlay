package models.framework.mapping;

import controllers.framework.mapping.Mapping.CustomConvertor;

import java.text.DecimalFormat;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import java.util.List;

import javax.annotation.Generated;

import models.framework.mapping.Bean.Inner;

@Generated(

    value = "org.mapstruct.ap.MappingProcessor",

    date = "2017-08-14T13:50:27+0900",

    comments = "version: 1.2.0.CR1, compiler: javac, environment: Java 1.8.0_144 (Oracle Corporation)"

)

public class BeanMapperImpl implements BeanMapper {

    private final CustomConvertor customConvertor = new CustomConvertor();

    @Override

    public Bean toBean(BeanFormContent beanFormContent) {

        if ( beanFormContent == null ) {

            return null;
        }

        Bean bean = new Bean();

        bean.setObject( innerToInner( beanFormContent.getObject() ) );

        if ( beanFormContent.getDateValue() != null ) {

            bean.setDateValue( DateTimeFormatter.ofPattern( "yyyy/MM/dd" ).format( beanFormContent.getDateValue() ) );
        }

        bean.setMapValue( beanFormContent.getMapValue() );

        bean.setStringValue( String.valueOf( beanFormContent.getIntValue() ) );

        List<String> list = beanFormContent.getStrings();

        if ( list != null ) {

            bean.setStrings( new ArrayList<String>( list ) );
        }

        else {

            bean.setStrings( null );
        }

        bean.setCustomValue( customConvertor.trim( beanFormContent.getCustomValue() ) );

        if ( beanFormContent.getStringValue() != null ) {

            bean.setIntValue( Integer.parseInt( beanFormContent.getStringValue() ) );
        }

        bean.setDtoValue( beanFormContent.getFormValue() );

        bean.setNumberValue( new DecimalFormat( "#,###" ).format( beanFormContent.getNumberValue() ) );

        bean.setValue( beanFormContent.getValue() );

        return bean;
    }

    protected Inner innerToInner(models.framework.mapping.BeanFormContent.Inner inner) {

        if ( inner == null ) {

            return null;
        }

        Inner inner1 = new Inner();

        inner1.setValue( inner.getValue() );

        return inner1;
    }
}

