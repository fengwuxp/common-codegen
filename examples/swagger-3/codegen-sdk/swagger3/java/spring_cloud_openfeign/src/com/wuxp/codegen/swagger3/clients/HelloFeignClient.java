package com.wuxp.codegen.swagger3.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;


    /**
     * 接口：GET
    **/

  @FeignClient(
        path = "/hello" 
  )
public interface HelloFeignClient{

    /**
      * 1:Http请求方法：GET
      * 2:Documented with OpenAPI v3 annotations
      * 3:标记忽略
      * 4:返回值在java中的类型为：String
     **/
      @GetMapping(value = "/hello" )
    String  index (
  );
}
