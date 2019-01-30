package com.wuxp.codegen.swagger2.example.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("订单")
@Data
public class Order {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("sn")
    private String sn;

    @ApiModelProperty(value = "下单用户")
    private User user;


    @ApiModelProperty("添加时间")
    public Date getAddTime(){
        return new Date();
    }
}
