package com.ms.sample.domain;

import com.custom.starter.web.exception.CustomRuntimeException;
import com.ms.sample.domain.Sample.SampleBuilder;
import com.ms.sample.domain.enums.SampleProcessStatus;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SampleTest {

  @Test
  @DisplayName("Test sample creation with mandatory properties and default values")
  public void sampleMandatoryProperties_ok() {
    //when
    Sample sampleName = new SampleBuilder()
        .name("sample_name")
        .build();

    //then
    SoftAssertions.assertSoftly(soft -> {
          assertThat(sampleName.getId()).isNotNull();
          assertThat(sampleName.getName()).isEqualTo("sample_name");
          assertThat(sampleName.getDescription()).isNull();
          assertThat(sampleName.getProcessStatus()).isEqualTo(SampleProcessStatus.ACCEPTED);
          assertThat(sampleName.getVersion()).isEqualTo(0);
        }
    );
  }

  @Test
  @DisplayName("Test sample creation with all properties")
  public void sampleAllProperties_ok() {
    //when
    Sample sampleName = new SampleBuilder()
        .id(UUID.fromString("812402f1-d3cf-4087-b779-2f659d45362c"))
        .name("sample_name")
        .description("sample_description")
        .processStatus(SampleProcessStatus.PROCESSED)
        .version(1)
        .build();

    //then
    SoftAssertions.assertSoftly(soft -> {
      assertThat(sampleName.getId()).isEqualTo(UUID.fromString("812402f1-d3cf-4087-b779-2f659d45362c"));
      assertThat(sampleName.getName()).isEqualTo("sample_name");
      assertThat(sampleName.getDescription()).isEqualTo("sample_description");
      assertThat(sampleName.getProcessStatus()).isEqualTo(SampleProcessStatus.PROCESSED);
      assertThat(sampleName.getVersion()).isEqualTo(1);
        }
    );
  }

  @Test
  @DisplayName("Test new builder use constructor with validations")
  public void sampleNewBuilder_useValidations() {
    //when
    Exception exception = catchException(() -> new SampleBuilder()
        .name("")
        .build());

    //then
    assertThat(exception).isInstanceOf(CustomRuntimeException.class);
  }

  @Test
  @DisplayName("Test to builder use constructor with validations")
  public void sampleToBuilder_useValidations() {
    Sample sampleName = new SampleBuilder()
        .name("sample_name")
        .build();

    assertThatThrownBy(()->
        sampleName.toBuilder()
        .name("")
        .build()).isExactlyInstanceOf(CustomRuntimeException.class);
  }

  @Test
  @DisplayName("Test id validations")
  public void sampleId_validations() {
    assertThatThrownBy(()->
        new SampleBuilder()
            .id(null)
            .name("sample_name")
            .build()).isExactlyInstanceOf(CustomRuntimeException.class);
  }

  @Test
  @DisplayName("Test name validations")
  public void sampleName_validations() {
    assertThatThrownBy(()->
        new SampleBuilder()
            .name(null)
            .build())
        .isExactlyInstanceOf(CustomRuntimeException.class);

    assertThatThrownBy(()->
        new SampleBuilder()
            .name("")
            .build())
        .isExactlyInstanceOf(CustomRuntimeException.class);

    assertThatThrownBy(()->
        new SampleBuilder()
            .name(RandomStringUtils.random(21))
            .build())
        .isExactlyInstanceOf(CustomRuntimeException.class);
  }

  @Test
  @DisplayName("Test description validations")
  public void sampleDescription_validations() {
    assertThatThrownBy(()->
        new SampleBuilder()
            .name("sample_name")
            .description(RandomStringUtils.random(256))
            .build())
        .isExactlyInstanceOf(CustomRuntimeException.class);
  }

  @Test
  @DisplayName("Test processStatus validations")
  public void sampleProcessStatus_validations() {
    assertThatThrownBy(()->
        new SampleBuilder()
            .name("sample_name")
            .processStatus(null)
            .build())
        .isExactlyInstanceOf(CustomRuntimeException.class);
  }
}