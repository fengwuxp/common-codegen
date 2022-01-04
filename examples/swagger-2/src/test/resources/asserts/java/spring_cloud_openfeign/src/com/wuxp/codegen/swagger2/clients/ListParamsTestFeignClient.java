package com.wuxp.codegen.swagger2.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

      import java.util.Set;
      import java.util.Collection;
      import java.util.List;
      import java.util.Map;

    /**
     * list tst
     * 接口：GET
     * list test（源码注释）
    **/

  @FeignClient(
        decode404 = false ,
        name = "exampleService" ,
        path = "/list" ,
        url = "${test.feign.url}" 
  )
public interface ListParamsTestFeignClient{

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @PostMapping()
    String  test1 (
          @RequestBody(required = true )  List<User> users
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GetMapping(value = "test_2" )
    String  test2 (
  User[] users
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GetMapping(value = "test_3" )
    String  test3 (
  Map<String,Order> users
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @PostMapping(value = "test_4" )
    String  test4 (
          @RequestBody(required = true )  Set<User> users
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @PostMapping(value = "test_5" )
    String  test5 (
          @RequestBody(required = true )  Collection<User> users
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @PostMapping(value = "test_6" )
    String  test6 (
          @RequestBody(required = true )  Set<User> users
  );
}
