package com.custom.starter.web.pagination.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public final class PageResponseDto<T> {

  private final List<T> content;
  private final List<PageOrderDto> orders;
  private final int totalPages;
  private final long totalElements;
  private final int pageNumber;
  private final int pageSize;
}

