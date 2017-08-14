package models.setting.user.changeuser;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ChangeUser.class)
public abstract class ChangeUser_ {

	public static volatile SingularAttribute<ChangeUser, LocalDateTime> expireDateTime;
	public static volatile SingularAttribute<ChangeUser, String> newUserId;
	public static volatile SingularAttribute<ChangeUser, String> code;
	public static volatile SingularAttribute<ChangeUser, Long> id;
	public static volatile SingularAttribute<ChangeUser, String> userId;

}

