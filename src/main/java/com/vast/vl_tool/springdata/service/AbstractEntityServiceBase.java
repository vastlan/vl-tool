package com.vast.vl_tool.springdata.service;

import com.vast.vl_tool.springdata.entity.EntityBase;
import com.vast.vl_tool.time.DateTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author qmhc
 * @description
 * @created 2021-06-17 15:22
 */

public abstract class AbstractEntityServiceBase<T extends EntityBase, ID, R extends JpaRepository<T, ID>> {
  protected final R entityRepository;

  public AbstractEntityServiceBase(R repository) {
    this.entityRepository = repository;
  }

  public List<T> list() {
    return entityRepository.findAll();
  }

  public List<T> listByIds(Iterable<ID> idList) {
    return entityRepository.findAllById(idList);
  }

  public List<T> createAll(List<T> entityList) {
    entityList.forEach(entity -> recordManipulationDate(entity));
    return entityRepository.saveAllAndFlush(entityList);
  }

  public List<T> updateAll(List<T> entityList) {
    entityList.forEach(entity -> recordManipulationDate(entity));
    return entityRepository.saveAllAndFlush(entityList);
  }

  public T get(ID id) {
    Assert.notNull(id, "Entity's id must not be null when select entity");

    return entityRepository.findById(id).orElse(null);
  }

  @Transactional
  public T create(T entity) {
    Assert.isNull(entity.getId(), "Entity's id must be null when create entity");

    recordManipulationDate(entity);
    return entityRepository.save(entity);
  }

  @Transactional
  public void delete(T entity) {
    Assert.notNull(entity, "cannot delete null entity");
    entityRepository.delete(entity);
  }

  @Transactional
  public void deleteAll(List<T> entityList) {
    Assert.notNull(entityList, "cannot delete null entity list");
    entityRepository.deleteAllInBatch(entityList);
  }

  @Transactional
  public void delete(ID id) {
    Assert.notNull(id, "Entity's id must not be null when delete entity");

    try {
      entityRepository.deleteById(id);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Transactional
  public T update(T entity) {
    Assert.notNull(entity.getId(), "Entity's id must not be null when update entity");

    recordManipulationDate(entity);

    try {
      mergeFromDatabase(entity);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }

    return updateEntityWithValid(entity);
  }

  private T updateEntityWithValid(T entity) {
    return entityRepository.save(entity);
  }

  protected void mergeFromDatabase(T sourceEntity) throws IllegalAccessException {
    ID id = (ID) sourceEntity.getId();
    T targetEntity = get(id);

    Assert.notNull(targetEntity, "cannot find target entity");

    Class<? extends EntityBase> sourceClass = sourceEntity.getClass();
    Class<? extends EntityBase> targetClass = targetEntity.getClass();

    Field[] sourceFields = sourceClass.getDeclaredFields();
    Field[] targetFields = targetClass.getDeclaredFields();

    for (int i = 0, len = sourceFields.length; i < len; i++) {
      Field sourceField = sourceFields[i];
      Field targetField = targetFields[i];

      if (Modifier.isStatic(sourceField.getModifiers()) || Modifier.isStatic(targetField.getModifiers())) {
        continue;
      }

      sourceField.setAccessible(true);
      targetField.setAccessible(true);

      if (!"serialVersionUID".equals(sourceField.getName()) && sourceField.get(sourceEntity) == null) {
        sourceField.set(sourceEntity, targetField.get(targetEntity));
      }
    }
  }

  protected void recordManipulationDate(T entity) {
    if (entity.getCreatedTime() == null) {
      entity.setCreatedTime(DateTool.getCurrentDateTimeOfDate());
    }

    entity.setLastModifiedTime(DateTool.getCurrentDateTimeOfDate());
  }
}
