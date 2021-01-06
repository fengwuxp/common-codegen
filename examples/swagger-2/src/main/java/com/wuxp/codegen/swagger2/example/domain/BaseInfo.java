package com.wuxp.codegen.swagger2.example.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseInfo<ID> {

  @ApiModelProperty("id")
  protected ID id;
}
