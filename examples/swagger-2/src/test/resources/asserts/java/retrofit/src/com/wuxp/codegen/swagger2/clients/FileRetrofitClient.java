package com.wuxp.codegen.swagger2.clients;
import retrofit2.http.*;


      import java.io.File;

    /**
     * 接口：GET
    **/

public interface FileRetrofitClient{

    /**
      * 1:GET /file/download
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：HttpEntity
      * 4:返回值在java中的类型为：InputStreamResource
     **/
      @GET(value = "/file/download" )
    File  download (
  String name
  );
    /**
      * 1:GET /file/download_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GET(value = "/file/download_2" )
      @Headers(value = {"Content-Type: application/octet-stream"} )
    File  download2 (
  String name
  );
}
