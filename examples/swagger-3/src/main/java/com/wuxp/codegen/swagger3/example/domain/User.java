package com.wuxp.codegen.swagger3.example.domain;


import com.wuxp.codegen.swagger3.example.enums.Sex;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class User {

    private Long id;

    private String name;

    private Integer age;


    private List<Order> orderList;

    private Sex sex;

    private Map<String,String> other;

    private Map other2;

    private List<?> list;

    private List list2;

    private String myFriends;


    public Boolean isBoy() {
        return false;
    }
}
