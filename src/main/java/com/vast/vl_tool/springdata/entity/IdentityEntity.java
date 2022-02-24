package com.vast.vl_tool.springdata.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author vastlan
 * @description 自增主键
 * @created 2022/2/24 16:25
 */

@MappedSuperclass
@Getter
@Setter
@ToString
public class IdentityEntity extends EntityBase<Integer> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
}
