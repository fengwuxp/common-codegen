package com.wuxp.codegen.swagger3.retrofits;
import retrofit2.http.*;


      import com.wuxp.codegen.swagger3.domain.User;
      import com.wuxp.codegen.swagger3.domain.BaseInfo;
      import java.util.List;
      import java.util.Map;
      import com.wuxp.codegen.swagger3.resp.ExampleDTO;

    /**
     * 接口：GET
     * example_cms
    **/

public interface ExampleService{

    /**
      * 1:GET /example_cms/get_num
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Integer
     **/
      @GET(value = "/example_cms/get_num" )
    List<Integer>  getNums (
          @Query(value = "num" )  Integer num
  );
    /**
      * 1:GET /example_cms/get_maps
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：Integer
      * 6:返回值在java中的类型为：String
     **/
      @GET(value = "/example_cms/get_maps" )
    List<Map<Integer,String>>  getMaps (
  );
    /**
      * 1:GET /example_cms/get_map
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Integer
     **/
      @GET(value = "/example_cms/get_map" )
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
      @GET(value = "/example_cms/get_map_2" )
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
      @GET(value = "/example_cms/get_map_3/{id}" )
    Map<String,List<Boolean>>  getMap3 (
          @Path(value = "id" )  String id
  );
    /**
      * 1:GET /example_cms/get_map_/
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GET(value = "/example_cms/get_map_/" )
    Map<String,List<Boolean>>  getMap4 (
          @QueryMap()  ExampleDTO dto
  );
    /**
      * 1:GET /example_cms/get_map_5
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GET(value = "/example_cms/get_map_5" )
    Map<String,List<Boolean>>  getMap5 (
          @QueryMap()  ExampleDTO dto
  );
    /**
      * 1:GET /example_cms/get_map_6
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GET(value = "/example_cms/get_map_6" )
    void  example0 (
          @Query(value = "req" )  BaseInfo<String,User> req
  );
    /**
      * 1:GET /example_cms/get_map_7
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GET(value = "/example_cms/get_map_7" )
    void  example2 (
          @Query(value = "req" )  Map<Long,User> req
  );
    /**
      * 1:GET /example_cms/get_map_8
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GET(value = "/example_cms/get_map_8" )
    void  example3 (
          @Query(value = "req" )  List<User> req
  );
}
