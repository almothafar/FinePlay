package models.base;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public interface EntityDao<ENTITY> {

	default long count(//
			@Nonnull final EntityManager manager, //
			@Nonnull final Class<ENTITY> entityClass) {

		final long count = count(manager, entityClass, (builder, query) -> {

			final Root<ENTITY> root = query.from(entityClass);
			query.select(builder.count(root));
		});
		return count;
	}

	default long count(//
			@Nonnull final EntityManager manager, //
			@Nonnull final Class<ENTITY> entityClass, @Nonnull final BiConsumer<CriteriaBuilder, CriteriaQuery<Long>> condition) {

		final long count = count(manager, entityClass, condition, typedQuery -> {
		});
		return count;
	}

	default long count(//
			@Nonnull final EntityManager manager, //
			@Nonnull final Class<ENTITY> entityClass, @Nonnull final BiConsumer<CriteriaBuilder, CriteriaQuery<Long>> condition, @Nonnull final Consumer<TypedQuery<Long>> parameters) {

		Objects.requireNonNull(manager);
		Objects.requireNonNull(entityClass);
		Objects.requireNonNull(condition);
		Objects.requireNonNull(parameters);

		final CriteriaBuilder builder = manager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		condition.accept(builder, query);
		final TypedQuery<Long> typedQuery = manager.createQuery(query);
		parameters.accept(typedQuery);
		return typedQuery.getSingleResult();
	}

	default void create(//
			@Nonnull final EntityManager manager, //
			@Nonnull final ENTITY entity) {

		Objects.requireNonNull(entity);

		manager.persist(entity);
	}

	@Nonnull
	default ENTITY read(//
			@Nonnull final EntityManager manager, //
			@Nonnull final Class<ENTITY> entityClass) {

		final ENTITY entity = read(manager, entityClass, (builder, query) -> {

			final Root<ENTITY> root = query.from(entityClass);
			query.select(root);
		});
		return entity;
	}

	@Nonnull
	default ENTITY read(//
			@Nonnull final EntityManager manager, //
			@Nonnull final Class<ENTITY> entityClass, @Nonnull final BiConsumer<CriteriaBuilder, CriteriaQuery<ENTITY>> condition) {

		final ENTITY entity = read(manager, entityClass, condition, typedQuery -> {
		});
		return entity;
	}

	@Nonnull
	default ENTITY read(//
			@Nonnull final EntityManager manager, //
			@Nonnull final Class<ENTITY> entityClass, @Nonnull final BiConsumer<CriteriaBuilder, CriteriaQuery<ENTITY>> condition, @Nonnull final Consumer<TypedQuery<ENTITY>> parameters) {

		Objects.requireNonNull(manager);
		Objects.requireNonNull(entityClass);
		Objects.requireNonNull(condition);
		Objects.requireNonNull(parameters);

		final CriteriaBuilder builder = manager.getCriteriaBuilder();
		final CriteriaQuery<ENTITY> query = builder.createQuery(entityClass);
		condition.accept(builder, query);
		final TypedQuery<ENTITY> typedQuery = manager.createQuery(query);
		parameters.accept(typedQuery);
		final ENTITY entity = typedQuery.getSingleResult();

		return entity;
	}

	@Nonnull
	default ENTITY update(//
			@Nonnull final EntityManager manager, //
			@Nonnull final ENTITY entity) {

		Objects.requireNonNull(entity);

		return manager.merge(entity);
	}

	default void delete(//
			@Nonnull final EntityManager manager, //
			@Nonnull final ENTITY entity) {

		Objects.requireNonNull(entity);

		manager.remove(entity);
	}

	default void createList(//
			@Nonnull final EntityManager manager, //
			@Nonnull final List<ENTITY> entities) {

		Objects.requireNonNull(manager);
		Objects.requireNonNull(entities);

		entities.stream().forEach(entity -> manager.persist(entity));
	}

	@Nonnull
	default List<ENTITY> readList(//
			@Nonnull final EntityManager manager, //
			@Nonnull final Class<ENTITY> entityClass) {

		final List<ENTITY> entities = readList(manager, entityClass, (builder, query) -> {

			final Root<ENTITY> root = query.from(entityClass);
			query.select(root);
		});
		return entities;
	}

	@Nonnull
	default List<ENTITY> readList(//
			@Nonnull final EntityManager manager, //
			@Nonnull final Class<ENTITY> entityClass, @Nonnull final BiConsumer<CriteriaBuilder, CriteriaQuery<ENTITY>> condition) {

		final List<ENTITY> entities = readList(manager, entityClass, condition, typedQuery -> {
		});
		return entities;
	}

	@Nonnull
	default List<ENTITY> readList(//
			@Nonnull final EntityManager manager, //
			@Nonnull final Class<ENTITY> entityClass, @Nonnull final BiConsumer<CriteriaBuilder, CriteriaQuery<ENTITY>> condition, @Nonnull final Consumer<TypedQuery<ENTITY>> parameters) {

		Objects.requireNonNull(manager);
		Objects.requireNonNull(entityClass);
		Objects.requireNonNull(condition);
		Objects.requireNonNull(parameters);

		final CriteriaBuilder builder = manager.getCriteriaBuilder();
		final CriteriaQuery<ENTITY> query = builder.createQuery(entityClass);
		condition.accept(builder, query);
		final TypedQuery<ENTITY> typedQuery = manager.createQuery(query);
		parameters.accept(typedQuery);
		final List<ENTITY> entities = typedQuery.getResultList();

		return entities;
	}

	@Nonnull
	default List<ENTITY> updateList(//
			@Nonnull final EntityManager manager, //
			@Nonnull final List<ENTITY> entities) {

		Objects.requireNonNull(entities);

		return entities.stream().map(entity -> manager.merge(entity)).collect(Collectors.toList());
	}

	default void deleteList(//
			@Nonnull final EntityManager manager, //
			@Nonnull final List<ENTITY> entities) {

		Objects.requireNonNull(manager);
		Objects.requireNonNull(entities);

		entities.stream().forEach(entity -> manager.remove(entity));
	}
}
