package com.wuxp.codegen.swagger2.example.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 这是一个ExampleEnum
 *
 * @author wuxp
 */
@ApiModel("ExampleEnum")
@AllArgsConstructor
@Getter
public enum ExampleEnum {

    /**
     * 男的
     */
    MAN(),

    /**
     * 女的
     *
     * @serialField 妹纸
     */
    @ApiModelProperty("女")
    WOMAN(),

    /**
     * ???
     */
    NONE();


}
