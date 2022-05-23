package com.vast.vl_tool.springdata.service;

import com.vast.vl_tool.springdata.entity.EntityBase;

import java.util.List;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 15:33
 */

public interface EntityServiceBase<T extends EntityBase<ID>, ID> {
  List<T> list();

  List<T> createAll(List<T> entityList);

  List<T> updateAll(List<T> entityList);

  T get(ID id);

  T create(T entity);

  T update(T entity);

  void delete(T entity);

  void deleteAll(List<T> entityList);

  void delete(ID id);
}
