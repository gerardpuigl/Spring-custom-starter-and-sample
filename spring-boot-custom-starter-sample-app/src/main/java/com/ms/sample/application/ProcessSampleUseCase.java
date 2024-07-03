package com.ms.sample.application;


import com.custom.starter.web.exception.CustomRuntimeException;
import com.ms.sample.application.income.ProcessSampleInPort;
import com.ms.sample.application.outcome.SampleEventOutPort;
import com.ms.sample.application.outcome.SampleEventOutPort.EventType;
import com.ms.sample.application.outcome.SamplePersistenceOutPort;
import com.ms.sample.domain.Sample;
import com.ms.sample.domain.enums.SampleProcessStatus;
import com.ms.sample.infraestructure.error.ErrorCodeEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessSampleUseCase implements ProcessSampleInPort {

  private final SamplePersistenceOutPort persistence;
  private final SampleEventOutPort sampleEventOutPort;

  private final EntityManagerFactory entityManagerFactory;

  @Override
  public void execute(UUID sampleId) {
    Sample sample = getSample(sampleId);

    EntityManager entityManager = entityManagerFactory.createEntityManager();

    EntityTransaction transaction1 = entityManager.getTransaction();
    transaction1.begin();
    checkIfProcessed(sample);
    sample = updateToInProgress(sample);
    transaction1.commit();

    EntityTransaction transaction2 = entityManager.getTransaction();
    processSample(sample);
    transaction2.commit();

    entityManager.close();
  }

  private Sample getSample(UUID sampleId) {
    return persistence.findById(sampleId)
        .orElseThrow(() -> new CustomRuntimeException(
            ErrorCodeEnum.SAMPLE_NOT_FOUND.getErrorCode(),
            "Sample with id %s not found, this should never happen.".formatted(sampleId)));
  }

  private void checkIfProcessed(Sample sample) {
    SampleProcessStatus processStatus = sample.getProcessStatus();
    if (processStatus.equals(SampleProcessStatus.IN_PROGRESS) || processStatus.equals(SampleProcessStatus.PROCESSED)) {
      throw new CustomRuntimeException(ErrorCodeEnum.SAMPLE_PROCESS_ALREADY_INITIATED.getErrorCode(),
          "Sample with id %s is already in progress or processed.".formatted(sample.getId()));
    }
  }

  private Sample updateToInProgress(Sample sample) {
    sample.setProcessStatus(SampleProcessStatus.IN_PROGRESS);
    sample = persistence.save(sample);
    sampleEventOutPort.publishSampleEvent(sample, EventType.SAMPLE_UPDATED_IN_PROGRESS);
    return sample;
  }

  private void processSample(Sample sample) {
    log.info("Sample id {} is being processed...", sample.getId());

    try {
      //simulate complex processing
      simulateProcessing(sample);
      log.info("Sample id {} processed successfully.", sample.getId());

      sample.setProcessStatus(SampleProcessStatus.PROCESSED);
      sampleEventOutPort.publishSampleEvent(sample, EventType.SAMPLE_UPDATED_PROCESSED);

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      sample.setProcessStatus(SampleProcessStatus.FAILED);
      sampleEventOutPort.publishSampleEvent(sample, EventType.SAMPLE_UPDATED_FAILED);
    }
    persistence.save(sample);
  }

  private static void simulateProcessing(Sample sample) throws InterruptedException {
    //just to simulate internal or external sample processing
    Thread.sleep(5000);

    //just to simulate failure use case
    if (sample.getDescription().contains("unstable")) {
      throw new CustomRuntimeException(
          ErrorCodeEnum.SAMPLE_PROCESS_FAILED.getErrorCode(),
          "Sample with id %s broken, process failed.".formatted(sample.getId()));
    }
  }
}
