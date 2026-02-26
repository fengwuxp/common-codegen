package com.wuxp.codegen.swagger3.clients;

import org.springframework.web.service.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import com.wind.client.rest.annotation.SpringQueryMap;


    /**
     * 接口：GET
    **/

  @HttpExchange(
        value = "/hello" 
  )
public interface HelloClient{

    /**
      * 1:GET /hello/hello
      * 2:Http请求方法：GET
      * 3:Documented with OpenAPI v3 annotations
      * 4:标记忽略
      * 5:返回值在java中的类型为：String
     **/
      @GetExchange(value = "/hello" )
    String  index (
  );
}
