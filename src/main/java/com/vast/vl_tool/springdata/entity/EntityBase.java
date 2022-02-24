package com.vast.vl_tool.springdata.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 15:23
 */

@MappedSuperclass
@Getter
@Setter
@ToString
public abstract class EntityBase<ID> implements IdEntity<ID> {

  private Date createdTime;

  private Date lastModifiedTime;

  private String createdUserId;

  private String lastModifiedUserId;

}
