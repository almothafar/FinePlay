package models.registuser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RegistUser.class)
public abstract class RegistUser_ {

	public static volatile SingularAttribute<RegistUser, LocalDateTime> expireDateTime;
	public static volatile SingularAttribute<RegistUser, String> salt;
	public static volatile SingularAttribute<RegistUser, String> code;
	public static volatile SingularAttribute<RegistUser, String> hashedPassword;
	public static volatile SingularAttribute<RegistUser, ZoneId> zoneId;
	public static volatile SingularAttribute<RegistUser, Long> id;
	public static volatile SingularAttribute<RegistUser, Locale> locale;
	public static volatile SingularAttribute<RegistUser, String> userId;

}

