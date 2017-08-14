package models.framework.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DateTime.class)
public abstract class DateTime_ {

	public static volatile SingularAttribute<DateTime, LocalDateTime> dateTime;
	public static volatile SingularAttribute<DateTime, LocalDate> date;
	public static volatile SingularAttribute<DateTime, Long> user_Id;
	public static volatile SingularAttribute<DateTime, LocalTime> time;

}

