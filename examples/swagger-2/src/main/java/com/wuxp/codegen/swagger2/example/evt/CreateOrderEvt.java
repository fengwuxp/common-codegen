package com.wuxp.codegen.swagger2.example.evt;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel("创建订单")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateOrderEvt extends BaseEvt {

    @ApiModelProperty(value = "订单ns", example = "test method", required = true)
    @Size(max = 50)
    private String sn;

    @ApiModelProperty("订单总价")
    @NotNull
    private Integer totalAmount;

}
