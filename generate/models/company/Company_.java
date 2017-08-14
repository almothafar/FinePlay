package models.company;

import java.time.LocalDateTime;
import java.util.Locale;
import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.company.organization.Organization;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Company.class)
public abstract class Company_ {

	public static volatile MapAttribute<Company, Locale, String> names;
	public static volatile SingularAttribute<Company, Organization> organization;
	public static volatile SingularAttribute<Company, Long> id;
	public static volatile SingularAttribute<Company, LocalDateTime> updateDateTime;

}

