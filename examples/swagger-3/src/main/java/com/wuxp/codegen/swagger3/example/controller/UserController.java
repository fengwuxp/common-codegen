package com.wuxp.codegen.swagger3.example.controller;


import com.wuxp.codegen.swagger3.example.domain.Order;
import com.wuxp.codegen.swagger3.example.domain.User;
import com.wuxp.codegen.swagger3.example.enums.Sex;
import com.wuxp.codegen.swagger3.example.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.resp.ServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 通过这里配置使下面的映射都在/users下，可去除
 */
@RestController
@RequestMapping(value = "/users")
@Tag(name = "user")
public class UserController {

    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    static {
        User value = new User();
        value.setMyFriends("张三");
        users.put(1L, value);
    }


    @Operation(
            summary = ""
    )
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        return users.get(id);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String putUser(@PathVariable Long id, @RequestBody User user, @RequestBody Order order) {
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

    @RequestMapping(value = "sample3", method = RequestMethod.GET)
    public String[] sample2(Long[] ids, String name) {
        return null;
    }

    @RequestMapping(value = "sample2", method = RequestMethod.GET)
    public Map<String, User[]>[] sampleMap(Long[] ids,
                                           String name,
                                           Sex sex,
                                           Map<String, String[]>[] testParam) {
        return null;
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


    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public ServiceResponse<List<PageInfo<User>>> test4(Long id) {

        return null;
    }

    @RequestMapping(value = "/test5", method = RequestMethod.GET)
    public Map<PageInfo<User>, List<PageInfo<User>>> test5(Long id) {
        return null;
    }

    @RequestMapping(value = "/test6", method = RequestMethod.GET)
    public Map<PageInfo<User[]>, List<PageInfo<User[]>>> test6(Long id) {
        return null;
    }

    @RequestMapping(value = "/test7", method = RequestMethod.GET)
    public Map<PageInfo<User[][]>, List<PageInfo<User[][]>>> test7(Long id) {

        return null;
    }

    @RequestMapping(value = "/test8", method = RequestMethod.GET)
    public Map<String[], String[][][][]> test8(Long id) {

        return null;
    }

    @RequestMapping(value = "/test9", method = RequestMethod.GET)
    public Map<String[][], String[]>[][][] test9(Long id) {

        return null;
    }
}
