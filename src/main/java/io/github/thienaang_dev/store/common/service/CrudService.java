package io.github.thienaang_dev.store.common.service;

import java.util.List;

/**
 * Common service interface for CRUD operations
 *
 * @param <T> entity type
 * @param <UUID> id type
 */
public interface CrudService<T, UUID> {

  /**
   * Create a new entity
   *
   * @param entity entity to create
   * @return entity created
   */
  T create(T entity);

  /**
   * Get an entity by id or throw exception if not found
   *
   * @param id entity's id to find
   * @return entity found
   */
  T getById(UUID id);

  /**
   * Get all entities
   *
   * @return list of entities
   */
  List<T> getAll();

  /**
   * Update an entity by id or throw exception if not found
   *
   * @param id entity's id to update
   * @param entity entity's updated details
   * @return entity updated
   */
  T update(UUID id, T entity);

  /**
   * Soft-delete entity by id or throw not found exception
   *
   * @param id entity's id to delete
   */
  void delete(UUID id);
}
