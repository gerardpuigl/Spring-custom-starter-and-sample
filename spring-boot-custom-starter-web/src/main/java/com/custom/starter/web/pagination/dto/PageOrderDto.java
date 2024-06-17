package com.custom.starter.web.pagination.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class PageOrderDto {

  private final String property;
  private final String direction;

}
