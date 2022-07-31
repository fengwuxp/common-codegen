package com.wuxp.codegen.swagger2.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

      import com.wuxp.codegen.swagger2.domain.Order;
      import com.wuxp.codegen.swagger2.domain.User;
      import java.util.List;
      import java.io.File;
      import java.util.Map;

    /**
     * 用户服务
     * 接口：GET
     * 用户服务（源码注释）
    **/

  @FeignClient(
        decode404 = false ,
        name = "exampleService" ,
        path = "/users" ,
        url = "${test.feign.url}" 
  )
public interface UserFeignClient{

    /**
      * 1:获取用户列表
      * 2:Http请求方法：GET
      * 3:获取用户列表信息
      * @return 用户列表
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：User
     **/
      @GetMapping()
    List<User>  getUserList (
  );
    /**
      * 1:创建用户
      * 2:属性名称：user，属性说明：用户详细实体user，默认值：，示例输入：
      * 3:Http请求方法：POST
      * 4:根据前端的提交内容创建用户
      * @return 用户Id
      * 6:返回值在java中的类型为：Long
     **/
      @PostMapping()
    Long  postUser (
          @RequestBody()  User user,
  Order order
  );
    /**
      * 1:获取用户详细信息
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：User
     **/
      @GetMapping(value = "/{id}" )
    User  getUser (
          @PathVariable(name = "id" )  Long id
  );
    /**
      * 1:更新用户详细信息
      * <pre>
      * 3:参数列表：
      * 4:参数名称：id，参数说明：null
      * 5:参数名称：user，参数说明：null
      * </pre>
      * 7:Http请求方法：PUT
      * 8:返回值在java中的类型为：String
     **/
      @PutMapping(value = "/{id}" )
    String  putUser (
          @PathVariable(name = "id" ,required = false )  Long id,
          @RequestBody()  User user
  );
    /**
      * 1:删除用户
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：DELETE
      * 4:返回值在java中的类型为：String
     **/
      @DeleteMapping(value = "/{id}" )
    String  deleteUser (
          @PathVariable(name = "id" )  Long id,
  String name
  );
    /**
      * 1:sample
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：String
     **/
      @GetMapping()
    String  sample (
  Long[] ids,
  String name
  );
    /**
      * 1:sample
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：User
     **/
      @GetMapping(value = "sample2" )
    Map<String,User>  sampleMap (
  Long[] ids,
  String name,
  Map<String,String[]>[] testParam
  );
    /**
      * 1:文件上传
      * 2:属性名称：file，属性说明：文件，默认值：，示例输入：
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：void
     **/
      @PostMapping(produces = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    void  uploadFile (
          @RequestParam(name = "file" )  File multipartFile
  );
    /**
      * 1:test3
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：Object
     **/
      @GetMapping(value = "/test" )
    Map<String,Object>  test3 (
  Long id
  );
}
