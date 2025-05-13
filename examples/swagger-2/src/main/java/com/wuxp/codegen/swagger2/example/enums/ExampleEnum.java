package com.wuxp.codegen.swagger2.example.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * 这是一个ExampleEnum
 *
 * @author wuxp
 */
@ApiModel("ExampleEnum")
@Getter
public enum ExampleEnum {

    /**
     * 男的
     */
    MAN,

    /**
     * 女的
     *
     * @serialField 妹纸
     */
    @ApiModelProperty("女")
    WOMAN,

    /**
     * 位置
     */
    NONE;


}
