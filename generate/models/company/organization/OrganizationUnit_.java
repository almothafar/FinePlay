package models.company.organization;

import java.time.LocalDateTime;
import java.util.Locale;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrganizationUnit.class)
public abstract class OrganizationUnit_ {

	public static volatile SingularAttribute<OrganizationUnit, OrganizationUnit> parent;
	public static volatile MapAttribute<OrganizationUnit, Locale, String> names;
	public static volatile ListAttribute<OrganizationUnit, OrganizationUnit> children;
	public static volatile SingularAttribute<OrganizationUnit, Long> sortOrder;
	public static volatile SingularAttribute<OrganizationUnit, Organization> organization;
	public static volatile SingularAttribute<OrganizationUnit, Long> id;
	public static volatile SingularAttribute<OrganizationUnit, LocalDateTime> updateDateTime;

}

