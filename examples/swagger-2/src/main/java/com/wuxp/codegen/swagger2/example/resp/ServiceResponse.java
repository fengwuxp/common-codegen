package com.wuxp.codegen.swagger2.example.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("服务响应对象")
@Data
public class ServiceResponse<T> {

    @ApiModelProperty("响应数据消息")
    String message;

    @ApiModelProperty("响应数据code")
    Integer code;

    @ApiModelProperty("响应数据")
    T data;

    public T getData() {
        return data;
    }
}
