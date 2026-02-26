package com.wuxp.codegen.swagger3.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

      import com.wuxp.codegen.swagger3.domain.Order;
      import com.wuxp.codegen.swagger3.domain.User;
      import com.wuxp.codegen.swagger3.enums.Sex;
      import com.wuxp.codegen.swagger3.resp.ServiceResponse;
      import com.wuxp.codegen.swagger3.resp.PageInfo;
      import java.util.List;
      import java.util.Map;
      import java.io.File;

    /**
     * 接口：GET
     * user
     * 通过这里配置使下面的映射都在/users下，可去除
    **/

  @FeignClient(
        path = "/users" 
  )
public interface UserFeignClient{

    /**
      * 1:GET /users/{id}
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：User
     **/
      @GetMapping(value = "/{id}" )
    User  getUser (
          @PathVariable(name = "id" )  Long id
  );
    /**
      * 1:PUT /users/{id}
      * 2:Http请求方法：PUT
      * 3:返回值在java中的类型为：String
     **/
      @PutMapping(value = "/{id}" )
    String  putUser (
          @PathVariable(name = "id" )  Long id,
          @RequestBody()  User user,
          @RequestBody()  Order order
  );
    /**
      * 1:DELETE /users/{id}
      * 2:Http请求方法：DELETE
      * 3:返回值在java中的类型为：String
     **/
      @DeleteMapping(value = "/{id}" )
    String  deleteUser (
          @PathVariable(name = "id" )  Long id,
  String name
  );
    /**
      * 1:GET /users/sample
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/
      @GetMapping()
    String  sample (
  Long[] ids,
  String name
  );
    /**
      * 1:GET /users/sample3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：String
     **/
      @GetMapping(value = "sample3" )
    String  sample2 (
  Long[] ids,
  String name
  );
    /**
      * 1:GET /users/sample2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：User
     **/
      @GetMapping(value = "sample2" )
    Map<String,User>  sampleMap (
  Long[] ids,
  String name,
  Sex sex,
  Map<String,String[]>[] testParam
  );
    /**
      * 1:POST /users/uploadFile
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：void
     **/
      @PostMapping(produces = {[HttpMediaType.MULTIPART_FORM_DATA]} )
    void  uploadFile (
          @RequestParam(name = "file" )  File multipartFile
  );
    /**
      * 1:GET /users/test
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Object
     **/
      @GetMapping(value = "/test" )
    Map<String,Object>  test3 (
  Long id
  );
    /**
      * 1:GET /users/test2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：User
     **/
      @GetMapping(value = "/test2" )
    ServiceResponse<List<PageInfo<User>>>  test4 (
  Long id
  );
    /**
      * 1:GET /users/test5
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：User
     **/
      @GetMapping(value = "/test5" )
    Map<String,List<PageInfo<User>>>  test5 (
  Long id
  );
    /**
      * 1:GET /users/test6
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：User
     **/
      @GetMapping(value = "/test6" )
    Map<Sex,List<PageInfo<User>>>  test6 (
  Long id
  );
    /**
      * 1:GET /users/test7
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Integer
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：User
     **/
      @GetMapping(value = "/test7" )
    Map<Integer,List<PageInfo<User>>>  test7 (
  Long id
  );
    /**
      * 1:GET /users/test8
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：String
     **/
      @GetMapping(value = "/test8" )
    Map<String,String>  test8 (
  Long id
  );
    /**
      * 1:GET /users/test9
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：Map
      * 7:返回值在java中的类型为：String
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：String
     **/
      @GetMapping(value = "/test9" )
    Map<String,String>  test9 (
  Long id
  );
}
