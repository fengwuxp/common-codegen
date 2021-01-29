package com.wuxp.codegen.swagger2.example.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApiModel("性别")
@AllArgsConstructor
@Getter
public enum Sex {

    @ApiModelProperty("男")
    MAN("男1"),

    @ApiModelProperty("女")
    WOMAN("女1"),

    @ApiModelProperty("未知")
    NONE("未知1");

    private String desc;


}
