package models.resetuser;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ResetUser.class)
public abstract class ResetUser_ {

	public static volatile SingularAttribute<ResetUser, LocalDateTime> expireDateTime;
	public static volatile SingularAttribute<ResetUser, String> code;
	public static volatile SingularAttribute<ResetUser, Long> id;
	public static volatile SingularAttribute<ResetUser, String> userId;

}

