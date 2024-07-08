package com.ms.sample.adapter.outcome.persistence.repository;

import com.ms.sample.adapter.outcome.persistence.dbo.SampleDbo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleJpaRepository extends JpaRepository<SampleDbo, UUID>, JpaSpecificationExecutor<SampleDbo> {

}
