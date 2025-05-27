package com.vast.vl_tool.springdata.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * @author vastlan
 * @description 字符串UUID主键实体
 * @created 2022/2/24 16:25
 */

@MappedSuperclass
@Getter
@Setter
@ToString
public class UUIDStrBaseEntity extends EntityBase<String> {

  @Id
  @GenericGenerator(name = "uuid-String-generator", strategy = "uuid")
  @GeneratedValue(generator = "uuid-String-generator")
  @Column(updatable = false)
  private String id;

}
