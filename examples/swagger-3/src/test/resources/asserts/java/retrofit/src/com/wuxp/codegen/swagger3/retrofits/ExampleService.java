package com.wuxp.codegen.swagger3.retrofits;
import retrofit2.http.*;


      import java.util.List;
      import com.wuxp.codegen.swagger3.ExampleDto;
      import com.wuxp.codegen.swagger3.evt.ExampleDTO;
      import java.util.Map;

    /**
     * 接口：GET
     * example_cms
    **/

public interface ExampleService{

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：List
      * 3:返回值在java中的类型为：Integer
     **/
      @GET(value = "/example_cms/get_num" )
    List<Integer>  getNums (
  Integer num
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：List
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Integer
      * 5:返回值在java中的类型为：String
     **/
      @GET(value = "/example_cms/get_maps" )
    List<Map<Integer,String>>  getMaps (
  Integer num
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：Integer
     **/
      @GET(value = "/example_cms/get_map" )
    Map<String,Integer>  getMap (
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Boolean
     **/
      @GET(value = "/example_cms/get_map_2" )
    Map<String,List<Boolean>>  getMap2 (
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Boolean
     **/
      @GET(value = "/example_cms/get_map_3/{id}" )
    Map<String,List<Boolean>>  getMap3 (
          @PathVariable(value = "id" )  String id
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Boolean
     **/
      @GET(value = "/example_cms/get_map_/" )
    Map<String,List<Boolean>>  getMap4 (
  ExampleDTO dto
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Boolean
     **/
      @GET(value = "/example_cms/get_map_5" )
    Map<String,List<Boolean>>  getMap5 (
  ExampleDto dto
  );
}
