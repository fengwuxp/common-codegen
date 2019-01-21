package com.wuxp.codegen.example.evt;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel("测试的API接口方法一的请求参数")
public class QueryOrderEvt extends BaseQueryEvt {

    @ApiModelProperty(value = "订单sn", example = "test method", required = true)
    @Size(max = 50)
    private String sn;

    @ApiModelProperty(value = "id列表")
    private int[] ids;

}
