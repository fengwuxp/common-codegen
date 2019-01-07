package com.wuxp.codegen.example.evt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("统一的查询对象")
public class BaseQueryEvt extends BaseEvt {

    @ApiModelProperty("查询大小")
    Integer querySize;

    @ApiModelProperty("查询页码")
    Integer queryPage;

}
