package com.wuxp.codegen.example.domain;

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

    @ApiModelProperty("下单用户")
    private User user;


    public Date getAddTime(){
        return new Date();
    }
}
