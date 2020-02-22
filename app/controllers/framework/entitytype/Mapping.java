package controllers.framework.entitytype;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.security.auth.login.AccountException;

import controllers.user.UserService;
import models.base.EntityDao;
import models.framework.entitytype.Entity;
import models.framework.entitytype.Entity_;
import models.system.System.PermissionsAllowed;
import models.user.User;
import models.user.User_;
import play.db.jpa.JPAApi;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

@PermissionsAllowed
public class Mapping extends Controller {

	@Inject
	private MessagesApi messagesApi;

	@Inject
	private JPAApi jpaApi;

	@Inject
	private UserService userService;

	private final EntityDao<Entity> entityDao = new EntityDao<Entity>() {
	};

	public Result index(@Nonnull final Request request) {

		final Messages messages = messagesApi.preferred(request);
		messages.lang();

		mapping(request, messages);
		return ok("mapped.");
	}

	public void mapping(@Nonnull final Request request, @Nonnull final Messages messages) {

		jpaApi.withTransaction(manager -> {

			try {

				final User user;
				try {

					user = userService.read(manager, messages, request.session().get(User_.USER_ID).get());
				} catch (final AccountException e) {

					throw new RuntimeException(e);
				}

				Entity entity = new Entity();
				entity.setUser_Id(user.getId());

				final Entity storedEntity = manager.find(Entity.class, user.getId());
				if (Objects.isNull(storedEntity)) {

					setValues(entity);
					entityDao.create(manager, entity);

					System.out.println("---");
					System.out.println(entity.getUser_Id());
					System.out.println(entity.getLocale());
					System.out.println(entity.getZoneId());
					System.out.println(entity.getYear());
					System.out.println(entity.getYearMonth());
					System.out.println(entity.getMonth());
					System.out.println(entity.getDayOfWeek());
					System.out.println(entity.getDateTime());
					System.out.println(entity.getDate());
					System.out.println(entity.getTime());
					System.out.println(entity.toString());
				} else {

					setValues(storedEntity);
					entityDao.update(manager, storedEntity);

					System.out.println("---");
					System.out.println(storedEntity.getUser_Id());
					System.out.println(storedEntity.getLocale());
					System.out.println(storedEntity.getZoneId());
					System.out.println(storedEntity.getYear());
					System.out.println(storedEntity.getYearMonth());
					System.out.println(storedEntity.getMonth());
					System.out.println(storedEntity.getDayOfWeek());
					System.out.println(storedEntity.getDateTime());
					System.out.println(storedEntity.getDate());
					System.out.println(storedEntity.getTime());
					System.out.println(storedEntity.toString());
				}

				manager.flush();
				manager.clear();

				final Entity updatedEntity = entityDao.read(manager, Entity.class, (builder, query) -> {

					final Root<models.framework.entitytype.Entity> root = query.from(models.framework.entitytype.Entity.class);

					final List<Predicate> predicates = new ArrayList<>();
					predicates.add(builder.equal(root.get(Entity_.user_Id), user.getId()));

					query.where(predicates.toArray(new Predicate[0]));
				});
				System.out.println("---");
				System.out.println(updatedEntity.getUser_Id());
				System.out.println(updatedEntity.getLocale());
				System.out.println(updatedEntity.getZoneId());
				System.out.println(updatedEntity.getYear());
				System.out.println(updatedEntity.getYearMonth());
				System.out.println(updatedEntity.getMonth());
				System.out.println(updatedEntity.getDayOfWeek());
				System.out.println(updatedEntity.getDateTime());
				System.out.println(updatedEntity.getDate());
				System.out.println(updatedEntity.getTime());
				System.out.println(updatedEntity.toString());

				final Object[] record = (Object[]) manager.createNativeQuery("SELECT * FROM " + Entity.NAME + " WHERE " + Entity_.USER__ID + "=?").setParameter(1, user.getId()).getSingleResult();
				System.out.println("---");
				System.out.println(record[0] + " " + (record[0] instanceof BigInteger ? "BigInteger" : "X"));
				System.out.println(record[1] + " " + (record[1] instanceof Date ? "Date" : "X"));// LocalDate
				System.out.println(record[2] + " " + (record[2] instanceof Timestamp ? "Timestamp" : "X"));// LocalDateTime
				System.out.println(record[3] + " " + (record[3] instanceof String ? "String" : "X"));// DayOfWeek
				System.out.println(record[4] + " " + (record[4] instanceof String ? "String" : "X"));// Locale
				System.out.println(record[5] + " " + (record[5] instanceof Integer ? "Integer" : "X"));// Month
				System.out.println(record[6] + " " + (record[6] instanceof Time ? "Time" : "X"));// LocalTime
				System.out.println(record[7] + " " + (record[7] instanceof Integer ? "Integer" : "X"));// Year
				System.out.println(record[8] + " " + (record[8] instanceof Timestamp ? "Timestamp" : "X"));// YearMonth
				System.out.println(record[9] + " " + (record[9] instanceof String ? "String" : "X"));// ZoneId
			} catch (final Exception e) {

				throw e;
			}
		});
	}

	private void setValues(final Entity entity) {

		entity.setLocale(Locale.US);

		entity.setZoneId(ZoneOffset.UTC);

//		entity.setDateTime(LocalDateTime.of(-123, Month.FEBRUARY, 2, 0, 0));
//		entity.setDate(LocalDate.of(-123, Month.FEBRUARY, 2));
//		entity.setTime(LocalTime.of(0, 0));
		entity.setDateTime(LocalDateTime.of(123, Month.FEBRUARY, 2, 0, 0));
		entity.setDate(LocalDate.of(123, Month.FEBRUARY, 2));
		entity.setTime(LocalTime.of(0, 0));

//		entity.setYear(Year.of(-123));
//		entity.setYearMonth(YearMonth.of(-123, Month.FEBRUARY));
//		entity.setMonth(Month.FEBRUARY);
//		entity.setDayOfWeek(LocalDate.of(-123, Month.FEBRUARY, 2).getDayOfWeek());
		entity.setYear(Year.of(123));
		entity.setYearMonth(YearMonth.of(123, Month.FEBRUARY));
		entity.setMonth(Month.FEBRUARY);
		entity.setDayOfWeek(LocalDate.of(123, Month.FEBRUARY, 2).getDayOfWeek());
	}
}
