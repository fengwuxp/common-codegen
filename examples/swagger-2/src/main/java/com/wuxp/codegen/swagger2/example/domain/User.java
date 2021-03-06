package com.wuxp.codegen.swagger2.example.domain;


import com.wuxp.codegen.swagger2.example.enums.ExampleEnum;
import com.wuxp.codegen.swagger2.example.enums.Sex;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

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

    @ApiModelProperty("其他")
    private Map<String, String> other;

    @ApiModelProperty("其他2")
    private Map other2;

    @ApiModelProperty("list")
    private List<?> list;

    private List list2;

    @ApiModelProperty("myFriends")
    private String myFriends;

    @ApiModelProperty("example enum")
    private ExampleEnum exampleEnum;


    @ApiModelProperty("是否为男孩")
    public Boolean isBoy() {
        return false;
    }
}
