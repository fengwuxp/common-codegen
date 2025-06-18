package com.wuxp.codegen.swagger2.example.controller;


import com.wuxp.codegen.swagger2.example.domain.Order;
import com.wuxp.codegen.swagger2.example.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户服务（源码注释）
 *
 * @author wuxp
 */
@Api("用户服务")
@RestController
// 通过这里配置使下面的映射都在/users下，可去除
@RequestMapping(value = "/users")
public class UserController {

    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    static {
        User value = new User();
        value.setMyFriends("张三");
        users.put(1L, value);
    }


    /**
     * 获取用户列表信息
     *
     * @return 用户列表
     */
    @ApiOperation(value = "获取用户列表", notes = "")
    @GetMapping()
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }

    /**
     * 根据前端的提交内容创建用户
     *
     * @param user 需要保存的用户信息
     * @return 用户Id
     */
    @ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Long postUser(@RequestBody User user, Order order) {
        users.put(user.getId(), user);
        return user.getId();
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        return users.get(id);
    }

    @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String putUser(@PathVariable(required = false) Long id, @RequestBody User user) {
        User u = users.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u);
        return "success";
    }

    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id, String name) {
        users.remove(id);
        return "success";
    }

    @ApiOperation(value = "sample", notes = "sample")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "sample", method = RequestMethod.GET)
    public String sample(Long[] ids, String name) {
        return "success";
    }

    @ApiOperation(value = "sample", notes = "sample")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "sample2", method = RequestMethod.GET)
    public Map<String, User> sampleMap(Long[] ids, String name, Map<String, String[]>[] testParam) {
        return null;
    }

    @ApiOperation(value = "文件上传", notes = "uploadFile")
    @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile")
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadFile(@RequestParam(name = "file") MultipartFile multipartFile) {

    }

    @ApiOperation(value = "test3", notes = "test3")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Map<String, Object> test3(Long id) {
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("userName", "id");
        stringStringHashMap.put("aH2", "112");
        return stringStringHashMap;
    }

}
