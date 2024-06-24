package com.ms.sample.adapter.outcome.persistence.dbo;

import com.custom.starter.persistence.entity.AuditEntity;
import com.ms.sample.adapter.outcome.persistence.dbo.enums.SampleTypeDbo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@EqualsAndHashCode(callSuper = true)
@Table(name = "sample")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleDbo extends AuditEntity {

  @Id
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "type", columnDefinition = "SAMPLE_TYPE")
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private SampleTypeDbo type;

}