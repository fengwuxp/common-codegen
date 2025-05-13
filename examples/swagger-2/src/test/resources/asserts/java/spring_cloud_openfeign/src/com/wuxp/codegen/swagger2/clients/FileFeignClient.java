package com.wuxp.codegen.swagger2.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

      import java.io.File;

    /**
     * 接口：GET
    **/

  @FeignClient(
        decode404 = false ,
        name = "exampleService" ,
        path = "/file" ,
        url = "${test.feign.url}" 
  )
public interface FileFeignClient{

    /**
      * 1:GET /file/download
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：HttpEntity
      * 4:返回值在java中的类型为：InputStreamResource
     **/
      @GetMapping(value = "/download" ,consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE} )
    File  download (
  String name
  );
    /**
      * 1:GET /file/download_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GetMapping(value = "/download_2" ,consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE} )
    File  download2 (
  String name
  );
}
