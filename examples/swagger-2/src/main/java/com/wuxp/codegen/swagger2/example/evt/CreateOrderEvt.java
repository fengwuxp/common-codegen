package com.wuxp.codegen.swagger2.example.evt;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@ApiModel("创建订单")
@Data
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateOrderEvt extends BaseEvt {

    @ApiModelProperty(value = "订单ns", example = "test method", required = true)
    @Size(max = 50)
    private String sn;

    @ApiModelProperty("订单总价")
    @NotNull
    private Integer totalAmount;

}
