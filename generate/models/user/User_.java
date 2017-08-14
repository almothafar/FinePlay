package models.user;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.company.Company;
import models.user.User.Role;
import models.user.User.Theme;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, LocalDateTime> expireDateTime;
	public static volatile SingularAttribute<User, String> salt;
	public static volatile SingularAttribute<User, String> hashedPassword;
	public static volatile SetAttribute<User, Role> roles;
	public static volatile SingularAttribute<User, Locale> locale;
	public static volatile SingularAttribute<User, String> userId;
	public static volatile SingularAttribute<User, LocalDateTime> signInDateTime;
	public static volatile SingularAttribute<User, ZoneId> zoneId;
	public static volatile SingularAttribute<User, Theme> theme;
	public static volatile SingularAttribute<User, Company> company;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, LocalDateTime> signOutDateTime;
	public static volatile SingularAttribute<User, LocalDateTime> updateDateTime;

}

