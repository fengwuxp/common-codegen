package com.wuxp.codegen.swagger2.clients;

import org.springframework.web.service.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import com.wind.client.rest.annotation.SpringQueryMap;

      import java.io.File;

    /**
     * 接口：GET
    **/

  @HttpExchange(
        value = "/file" 
  )
public interface FileClient{

    /**
      * 1:GET /file/download
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：HttpEntity
      * 4:返回值在java中的类型为：InputStreamResource
     **/
      @GetExchange(value = "/download" ,contentType = {[HttpMediaType.APPLICATION_STREAM]} )
    File  download (
  String name
  );
    /**
      * 1:GET /file/download_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GetExchange(value = "/download_2" ,contentType = {[HttpMediaType.APPLICATION_STREAM]} )
    File  download2 (
  String name
  );
}
