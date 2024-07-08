package com.ms.sample.application.income;

import com.ms.sample.domain.Sample;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetSamplesByParamsInPort {

  Page<Sample> execute(GetSampleParamRequest request, Pageable pageable);

  record GetSampleParamRequest(
      Optional<String> name,
      Optional<String> processStatus){
  }
}
