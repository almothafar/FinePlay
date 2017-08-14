package models.company.organization;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.company.Company;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Organization.class)
public abstract class Organization_ {

	public static volatile SetAttribute<Organization, OrganizationUnit> organizationUnits;
	public static volatile SingularAttribute<Organization, Company> company;
	public static volatile SingularAttribute<Organization, Long> id;
	public static volatile SingularAttribute<Organization, LocalDateTime> updateDateTime;

}

