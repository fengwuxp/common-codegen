package com.wuxp.codegen.example.resp;

import io.swagger.annotations.ApiModel;

@ApiModel("统一的查询响应对象")
public class ServiceQueryResponse<T> extends ServiceResponse<PageInfo<T>> {
}
