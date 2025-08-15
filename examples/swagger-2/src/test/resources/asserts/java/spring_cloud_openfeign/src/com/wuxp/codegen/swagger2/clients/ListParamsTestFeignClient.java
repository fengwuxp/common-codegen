package com.wuxp.codegen.swagger2.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

      import com.wuxp.codegen.swagger2.domain.Order;
      import com.wuxp.codegen.swagger2.domain.User;
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
      * 1:POST /list
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/
      @PostMapping()
    String  test1 (
          @RequestBody()  List<User> users
  );
    /**
      * 1:GET /list/test_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/
      @GetMapping(value = "test_2" )
    String  test2 (
  User[] users
  );
    /**
      * 1:GET /list/test_3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/
      @GetMapping(value = "test_3" )
    String  test3 (
  Map<String,Order> users
  );
    /**
      * 1:POST /list/test_4
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/
      @PostMapping(value = "test_4" )
    String  test4 (
          @RequestBody()  Set<User> users
  );
    /**
      * 1:POST /list/test_5
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/
      @PostMapping(value = "test_5" )
    String  test5 (
          @RequestBody()  Collection<User> users
  );
    /**
      * 1:POST /list/test_6
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/
      @PostMapping(value = "test_6" )
    String  test6 (
          @RequestBody()  Set<User> users
  );
}
