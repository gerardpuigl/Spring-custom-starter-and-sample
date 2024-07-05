package com.ms.sample.domain;

import com.custom.starter.web.exception.CustomRuntimeException;
import com.ms.sample.domain.Sample.SampleBuilder;
import com.ms.sample.domain.enums.SampleProcessStatus;
import java.util.UUID;
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
  @DisplayName("Sample with mandatory information and default values")
  public void test_sample_mandatory_ok() {
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
  @DisplayName("Sample with mandatory information and default values")
  public void test_sample_all_ok() {
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
  @DisplayName("New builder validation fails")
  public void test_sample_validation_fails() {
    //when
    Exception exception = catchException(() -> new SampleBuilder()
        .name("")
        .build());

    //then
    assertThat(exception).isInstanceOf(CustomRuntimeException.class);
  }

  @Test
  @DisplayName("to builder validation fails")
  public void test_sample_to_builder_and_build_validation_fails() {
    Sample sampleName = new SampleBuilder()
        .name("sample_name")
        .build();

    assertThatThrownBy(()->
        sampleName.toBuilder()
        .name("")
        .build()).isExactlyInstanceOf(CustomRuntimeException.class);
  }


}