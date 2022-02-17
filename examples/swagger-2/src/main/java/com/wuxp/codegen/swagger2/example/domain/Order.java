package com.wuxp.codegen.swagger2.example.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("订单")
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Order extends BaseInfo<Long> {

    @ApiModelProperty(value = "sn", example = "order_sn_199223")
    private String sn;

    @ApiModelProperty(value = "下单用户")
    private User user;

    @ApiModelProperty("添加时间")
    public Date getAddTime() {
        return new Date();
    }
}
