package com.wuxp.codegen.swagger2.example.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 这是一个性别的枚举
 *
 * @author wuxp
 */
@ApiModel("性别")
@AllArgsConstructor
@Getter
public enum Sex {

    /**
     * 男的
     */
    @ApiModelProperty("男")
    MAN("男1", 0, false),

    /**
     * 女的
     */
    @ApiModelProperty("女")
    WOMAN("女1", 1, true),

    /**
     * ???
     */
    @ApiModelProperty("未知")
    NONE("未知1", 2, false);

    private String desc;

    private Integer code;

    private boolean enabled;

}
