package com.wuxp.codegen.swagger3.example.domain;


import com.wuxp.codegen.swagger3.example.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Schema(title = "用户信息", description = "用户信息描述")
public class User {

    @Schema(title = "id", description = "用户ID")
    private Long id;

    @Schema(title = "姓名", description = "用户名称")
    private String name;

    private Integer age;


    private List<Order> orderList;

    private Sex sex;

    private Map<String, String> other;

    private Map other2;

    private List<?> list;

    private List list2;

    private String myFriends;


    public Boolean isBoy() {
        return false;
    }
}
