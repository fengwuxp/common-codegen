package com.wuxp.codegen.swagger3.example.controller;


import com.wuxp.codegen.swagger3.example.domain.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping(value = "/users")     // 通过这里配置使下面的映射都在/users下，可去除
public class UserController {

    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    static {
        User value = new User();
        value.setMyFriends("张三");
        users.put(1L, value);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        return users.get(id);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String putUser(@PathVariable Long id, @RequestBody User user) {
        User u = users.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u);
        return "success";
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id, String name) {
        users.remove(id);
        return "success";
    }


    @RequestMapping(value = "sample", method = RequestMethod.GET)
    public String sample(Long[] ids, String name) {
        return "success";
    }


    @RequestMapping(value = "uploadFile", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadFile(@RequestParam(name = "file") CommonsMultipartFile commonsMultipartFile) {

    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Map<String, Object> test3(Long id) {
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("userName", "id");
        stringStringHashMap.put("aH2", "112");
        return stringStringHashMap;
    }

}
