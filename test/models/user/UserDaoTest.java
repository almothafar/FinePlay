package models.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static play.test.Helpers.running;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import models.base.EntityDao;
import models.user.User.Role;
import models.user.User.Theme;
import play.db.jpa.JPAApi;
import play.test.Helpers;
import play.test.WithApplication;

public class UserDaoTest extends WithApplication {

	private JPAApi jpaApi;

	private UserDao userDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		jpaApi = instanceOf(JPAApi.class);

		userDao = new UserDao();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCount() {

		running(Helpers.testServer(), () -> {

			jpaApi.withTransaction(manager -> {

				long count;
				try {

					count = new EntityDao<User>() {
					}.count(manager, models.user.User.class);

					assertThat("", 104L, is(count));
				} catch (final Exception e1) {

					fail("");
				}

				try {

					count = new EntityDao<User>() {
					}.count(manager, models.user.User.class, (builder, query) -> {

						final Root<User> root = query.from(User.class);
						query.select(builder.count(root));
						query.where(builder.equal(root.get(User_.locale), Locale.US));
					});

					assertThat("", 103L, is(count));
				} catch (final Exception e1) {

					fail("");
				}

				try {

					count = new EntityDao<User>() {
					}.count(manager, models.user.User.class, (builder, query) -> {

						final Root<User> root = query.from(User.class);
						query.select(builder.count(root));
						query.where(builder.equal(root.get(User_.locale), Locale.US));
					}, parameters -> {

					});

					assertThat("", 103L, is(count));
				} catch (final Exception e1) {

					fail("");
				}
			});
		});
	}

	@Test
	public void testCRUD() {

		running(Helpers.testServer(), () -> {

			jpaApi.withTransaction(manager -> {

				final User createUser = new User();
				createUser.setUserId("test@example.com");
				createUser.setPassword("test1!aA");
				createUser.setRoles(EnumSet.of(Role.CUSTOMER));
				createUser.setLocale(Locale.US);
				createUser.setZoneId(ZoneId.of("UTC"));
				createUser.setTheme(Theme.DEFAULT);
				createUser.setExpireDateTime(LocalDateTime.MAX);
				createUser.setUpdateDateTime(LocalDateTime.now());
				try {

					userDao.create(manager, createUser);
				} catch (final Exception e) {

					fail("");
				}

				long count;
				try {

					count = userDao.count(manager, (builder, query) -> {

						final Root<User> root = query.from(User.class);
						query.select(builder.count(root));
						query.where(builder.equal(root.get(User_.userId), createUser.getUserId()));
					});

					assertThat("", 1L, is(count));
				} catch (final Exception e1) {

					fail("");
				}

				User readUser = null;
				try {

					readUser = userDao.read(manager, (builder, query) -> {

						final Root<User> root = query.from(User.class);
						query.where(builder.equal(root.get(User_.userId), createUser.getUserId()));
					});
				} catch (final Exception e) {

					fail("");
				}

				final User updateUser = readUser;
				updateUser.setRoles(EnumSet.of(Role.ADMIN));
				try {

					userDao.update(manager, updateUser);
				} catch (final Exception e) {

					fail("");
				}

				final User deleteUser = updateUser;
				try {

					userDao.delete(manager, deleteUser);
				} catch (final Exception e) {

					fail("");
				}

				try {

					count = userDao.count(manager, (builder, query) -> {

						final Root<User> root = query.from(User.class);
						query.select(builder.count(root));
						query.where(builder.equal(root.get(User_.userId), deleteUser.getUserId()));
					});

					assertThat("", 0L, is(count));
				} catch (final Exception e1) {

					fail("");
				}
			});
		});
	}

	@Test
	public void testRead1() {

		running(Helpers.testServer(), () -> {

			jpaApi.withTransaction(manager -> {

				final User readUser = new User();
				readUser.setUserId("test@example.com");
				readUser.setPassword("test1!aA");
				readUser.setRoles(EnumSet.of(Role.CUSTOMER));
				readUser.setLocale(Locale.US);
				readUser.setZoneId(ZoneId.of("UTC"));
				readUser.setTheme(Theme.DEFAULT);
				readUser.setExpireDateTime(LocalDateTime.MAX);
				readUser.setUpdateDateTime(LocalDateTime.now());
				try {

					userDao.create(manager, readUser);
				} catch (final Exception e) {

					fail("");
				}

				try {

					userDao.read(manager, User.class);
					fail("");
				} catch (final NonUniqueResultException e) {

					assertTrue(true);
				}

				userDao.delete(manager, readUser);
			});
		});
	}

	@Test
	public void testRead2() {

		running(Helpers.testServer(), () -> {

			jpaApi.withTransaction(manager -> {

				final User readUser = new User();
				readUser.setUserId("test@example.com");
				readUser.setPassword("test1!aA");
				readUser.setRoles(EnumSet.of(Role.CUSTOMER));
				readUser.setLocale(Locale.US);
				readUser.setZoneId(ZoneId.of("UTC"));
				readUser.setTheme(Theme.DEFAULT);
				readUser.setExpireDateTime(LocalDateTime.MAX);
				readUser.setUpdateDateTime(LocalDateTime.now());

				userDao.create(manager, readUser);

				final User user = userDao.read(manager, User.class, (builder, query) -> {

					final Root<models.user.User> root = query.from(models.user.User.class);

					final List<Predicate> predicates = new ArrayList<>();
					predicates.add(builder.equal(root.get(User_.userId), "test@example.com"));

					query.where(predicates.toArray(new Predicate[0]));
				});

				assertThat("", "test@example.com", is(user.getUserId()));

				userDao.delete(manager, readUser);
			});
		});
	}

	@Test
	public void testRead3() {

		running(Helpers.testServer(), () -> {

			jpaApi.withTransaction(manager -> {

				final User readUser = new User();
				readUser.setUserId("test@example.com");
				readUser.setPassword("test1!aA");
				readUser.setRoles(EnumSet.of(Role.CUSTOMER));
				readUser.setLocale(Locale.US);
				readUser.setZoneId(ZoneId.of("UTC"));
				readUser.setTheme(Theme.DEFAULT);
				readUser.setExpireDateTime(LocalDateTime.MAX);
				readUser.setUpdateDateTime(LocalDateTime.now());

				userDao.create(manager, readUser);

				final User user = userDao.read(manager, User.class, (builder, query) -> {

					final Root<models.user.User> root = query.from(models.user.User.class);

					final List<Predicate> predicates = new ArrayList<>();
					predicates.add(builder.equal(root.get(User_.userId), "test@example.com"));

					query.where(predicates.toArray(new Predicate[0]));
				}, parameters -> {

					parameters.setFirstResult(0);
				});

				assertThat("", "test@example.com", is(user.getUserId()));

				userDao.delete(manager, readUser);
			});
		});
	}

	@Test
	public void testReadList1() {

		running(Helpers.testServer(), () -> {

			jpaApi.withTransaction(manager -> {

				final User readUser = new User();
				readUser.setUserId("test@example.com");
				readUser.setPassword("test1!aA");
				readUser.setRoles(EnumSet.of(Role.CUSTOMER));
				readUser.setLocale(Locale.US);
				readUser.setZoneId(ZoneId.of("UTC"));
				readUser.setTheme(Theme.DEFAULT);
				readUser.setExpireDateTime(LocalDateTime.MAX);
				readUser.setUpdateDateTime(LocalDateTime.now());
				try {

					userDao.create(manager, readUser);
				} catch (final Exception e) {

					fail("");
				}

				final List<User> users = userDao.readList(manager, User.class);

				final List<User> filterUsers = users.stream().filter(user -> "test@example.com".equals(user.getUserId())).collect(Collectors.toList());
				assertThat("", 1, is(filterUsers.size()));
				assertThat("", "test@example.com", is(filterUsers.get(0).getUserId()));

				userDao.delete(manager, readUser);
			});
		});
	}

	@Test
	public void testReadList2() {

		running(Helpers.testServer(), () -> {

			jpaApi.withTransaction(manager -> {

				final User readUser = new User();
				readUser.setUserId("test@example.com");
				readUser.setPassword("test1!aA");
				readUser.setRoles(EnumSet.of(Role.CUSTOMER));
				readUser.setLocale(Locale.US);
				readUser.setZoneId(ZoneId.of("UTC"));
				readUser.setTheme(Theme.DEFAULT);
				readUser.setExpireDateTime(LocalDateTime.MAX);
				readUser.setUpdateDateTime(LocalDateTime.now());

				userDao.create(manager, readUser);

				final List<User> users = userDao.readList(manager, User.class, (builder, query) -> {

					final Root<models.user.User> root = query.from(models.user.User.class);

					final List<Predicate> predicates = new ArrayList<>();
					predicates.add(builder.equal(root.get(User_.userId), "test@example.com"));

					query.where(predicates.toArray(new Predicate[0]));
				});

				assertThat("", 1, is(users.size()));
				assertThat("", "test@example.com", is(users.get(0).getUserId()));

				userDao.delete(manager, readUser);
			});
		});
	}

	@Test
	public void testReadList3() {

		running(Helpers.testServer(), () -> {

			jpaApi.withTransaction(manager -> {

				final User readUser = new User();
				readUser.setUserId("test@example.com");
				readUser.setPassword("test1!aA");
				readUser.setRoles(EnumSet.of(Role.CUSTOMER));
				readUser.setLocale(Locale.US);
				readUser.setZoneId(ZoneId.of("UTC"));
				readUser.setTheme(Theme.DEFAULT);
				readUser.setExpireDateTime(LocalDateTime.MAX);
				readUser.setUpdateDateTime(LocalDateTime.now());

				userDao.create(manager, readUser);

				final List<User> users = userDao.readList(manager, User.class, (builder, query) -> {

					final Root<models.user.User> root = query.from(models.user.User.class);

					final List<Predicate> predicates = new ArrayList<>();
					predicates.add(builder.equal(root.get(User_.userId), "test@example.com"));

					query.where(predicates.toArray(new Predicate[0]));
				}, parameters -> {

					parameters.setFirstResult(0);
				});

				assertThat("", 1, is(users.size()));
				assertThat("", "test@example.com", is(users.get(0).getUserId()));

				userDao.delete(manager, readUser);
			});
		});
	}
}
