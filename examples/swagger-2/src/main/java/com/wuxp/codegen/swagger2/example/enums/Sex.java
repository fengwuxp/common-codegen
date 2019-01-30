package com.wuxp.codegen.swagger2.example.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("性别")
public enum  Sex {

    @ApiModelProperty("男")
    MAN,

    @ApiModelProperty("女")
    WOMAN,

    @ApiModelProperty("未知")
    NONE
}
