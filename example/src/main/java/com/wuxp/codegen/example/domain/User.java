package com.wuxp.codegen.example.domain;


import com.wuxp.codegen.example.enums.Sex;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("用户")
@Data
public class User {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("订单列表")
    private List<Order> orderList;

    @ApiModelProperty("性别")
    private Sex sex;


    public Boolean isBoy() {
        return false;
    }
}
