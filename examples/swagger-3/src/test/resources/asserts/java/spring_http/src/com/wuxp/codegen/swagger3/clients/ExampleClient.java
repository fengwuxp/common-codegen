package com.wuxp.codegen.swagger3.clients;

import org.springframework.web.service.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import com.wind.client.rest.annotation.SpringQueryMap;

      import com.wuxp.codegen.swagger3.domain.User;
      import com.wuxp.codegen.swagger3.domain.BaseInfo;
      import java.util.List;
      import java.util.Map;
      import com.wuxp.codegen.swagger3.resp.ExampleDTO;

    /**
     * 接口：GET
     * example_cms
    **/

  @HttpExchange(
        value = "/example_cms" 
  )
public interface ExampleClient{

    /**
      * 1:GET /example_cms/get_num
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Integer
     **/
      @GetExchange(value = "get_num" )
    List<Integer>  getNums (
          @RequestParam(name = "num" )  Integer num
  );
    /**
      * 1:GET /example_cms/get_maps
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：Integer
      * 6:返回值在java中的类型为：String
     **/
      @GetExchange(value = "get_maps" )
    List<Map<Integer,String>>  getMaps (
  );
    /**
      * 1:GET /example_cms/get_map
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Integer
     **/
      @GetExchange(value = "get_map" )
    Map<String,Integer>  getMap (
  );
    /**
      * 1:GET /example_cms/get_map_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GetExchange(value = "get_map_2" )
    Map<String,List<Boolean>>  getMap2 (
  );
    /**
      * 1:GET /example_cms/get_map_3/{id}
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GetExchange(value = "get_map_3/{id}" )
    Map<String,List<Boolean>>  getMap3 (
          @PathVariable(name = "id" )  String id
  );
    /**
      * 1:GET /example_cms/get_map_/
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GetExchange(value = "get_map_/" )
    Map<String,List<Boolean>>  getMap4 (
  ExampleDTO dto
  );
    /**
      * 1:GET /example_cms/get_map_5
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GetExchange(value = "get_map_5" )
    Map<String,List<Boolean>>  getMap5 (
  ExampleDTO dto
  );
    /**
      * 1:GET /example_cms/get_map_6
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GetExchange(value = "get_map_6" )
    void  example0 (
  BaseInfo<String,User> req
  );
    /**
      * 1:GET /example_cms/get_map_7
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GetExchange(value = "get_map_7" )
    void  example2 (
  Map<Long,User> req
  );
    /**
      * 1:GET /example_cms/get_map_8
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GetExchange(value = "get_map_8" )
    void  example3 (
  List<User> req
  );
}
