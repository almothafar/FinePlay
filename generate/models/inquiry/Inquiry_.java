package models.inquiry;

import java.time.LocalDateTime;
import java.util.Locale;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.inquiry.Inquiry.Type;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Inquiry.class)
public abstract class Inquiry_ {

	public static volatile SingularAttribute<Inquiry, LocalDateTime> dateTime;
	public static volatile SingularAttribute<Inquiry, String> name;
	public static volatile SingularAttribute<Inquiry, Long> id;
	public static volatile SingularAttribute<Inquiry, Locale> locale;
	public static volatile SingularAttribute<Inquiry, Type> type;
	public static volatile SingularAttribute<Inquiry, String> title;
	public static volatile SingularAttribute<Inquiry, String> userId;
	public static volatile SingularAttribute<Inquiry, String> content;

}

