package com.vast.vl_tool.springdata.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * @author vastlan
 * @description 字节码UUID主键
 * @created 2022/2/24 16:25
 */

@MappedSuperclass
@Getter
@Setter
@ToString
public class UUIDByteEntity extends EntityBase<UUID> {

  @Id
  @GenericGenerator(name = "uuid-byte-generator", strategy = "uuid2")
  @GeneratedValue(generator = "uuid-byte-generator")
  @Column(length = 16, updatable = false)
  private UUID id;
}
