package com.ms.sample.adapter.outcome.persistence;

import com.ms.sample.adapter.outcome.persistence.dbo.SampleDbo;
import com.ms.sample.application.outcome.SamplePersistenceOutPort.GetSampleParamRequest;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SampleSpecificationFactory {

  public Specification<SampleDbo> getSpecifications(GetSampleParamRequest request) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicateList = new ArrayList<>();

      request
          .name().ifPresent(
              name -> predicateList.add(criteriaBuilder.equal(root.get("name"), name)));

      request
          .processStatus().ifPresent(
              processStatus -> predicateList.add(criteriaBuilder.equal(root.get("processStatus").as(String.class), processStatus)));

      return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));
    };
  }

}
