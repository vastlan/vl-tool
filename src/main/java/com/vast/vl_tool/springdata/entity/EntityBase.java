package com.vast.vl_tool.springdata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 15:23
 */

public interface EntityBase<ID> {
  ID getId();

  Date getCreateTime();

  Date setCreatedTime(Date date);

  Date getModifiedTime();

  Date setModifiedTime(Date date);
}
