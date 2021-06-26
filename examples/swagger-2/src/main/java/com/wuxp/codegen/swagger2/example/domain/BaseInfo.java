package com.wuxp.codegen.swagger2.example.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseInfo<ID> {

    @ApiModelProperty("id \n我的 \\n 你的 \r 他的 \\r 不是的")
    protected ID id;

    protected BaseExample example;

    public enum BaseExample {

        A;
    }
}
