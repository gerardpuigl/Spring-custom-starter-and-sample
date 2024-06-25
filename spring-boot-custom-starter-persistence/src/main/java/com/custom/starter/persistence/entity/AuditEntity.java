package com.custom.starter.persistence.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity {

  @Column(name = "version")
  @Version
  private Integer version;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  @OptimisticLock(excluded = true) // avoid version increase by this field update
  private LocalDateTime createdDate;

  @Column(name = "last_modified_date")
  @LastModifiedDate
  @OptimisticLock(excluded = true) // avoid version increase by this field update
  private LocalDateTime lastModifiedDate;

  @PrePersist
  protected void onCreate() {
    version = 0;
  }
}
