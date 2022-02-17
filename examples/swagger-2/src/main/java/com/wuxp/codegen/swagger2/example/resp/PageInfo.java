package com.wuxp.codegen.swagger2.example.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("分页对象")
public class PageInfo<T> {

    @ApiModelProperty("响应集合列表")
    List<T> records;

    @ApiModelProperty("查询页码")
    Integer queryPage;

    @ApiModelProperty("查询大小")
    Integer querySize;

    public List<T> getRecords() {
        return records;
    }
}
