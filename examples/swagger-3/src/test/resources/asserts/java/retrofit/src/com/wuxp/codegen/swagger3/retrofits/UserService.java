package com.wuxp.codegen.swagger3.retrofits;
import retrofit2.http.*;


      import com.wuxp.codegen.swagger3.domain.Order;
      import com.wuxp.codegen.swagger3.domain.User;
      import com.wuxp.codegen.swagger3.enums.Sex;
      import com.wuxp.codegen.swagger3.resp.PageInfo;
      import com.wuxp.codegen.swagger3.resp.ServiceResponse;
      import java.util.List;
      import java.io.File;
      import java.util.Map;

    /**
     * 接口：GET
     * user
     * 通过这里配置使下面的映射都在/users下，可去除
    **/

public interface UserService{

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：User
     **/
      @GET(value = "/users/{id}" )
    Observable<User>  getUser (
          @Path(value = "id" )  Long id
  );
    /**
      * 1:Http请求方法：PUT
      * 2:返回值在java中的类型为：String
     **/
      @PUT(value = "/users/{id}" )
    Observable<String>  putUser (
          @Path(value = "id" )  Long id,
          @Body()  User user,
          @Body()  Order order
  );
    /**
      * 1:Http请求方法：DELETE
      * 2:返回值在java中的类型为：String
     **/
      @DELETE(value = "/users/{id}" )
    Observable<String>  deleteUser (
          @Path(value = "id" )  Long id,
          @Query(value = "name" )  String name
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GET(value = "/users/sample" )
    Observable<String>  sample (
          @Query(value = "ids" )  Long[] ids,
          @Query(value = "name" )  String name
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：数组
      * 3:返回值在java中的类型为：String
     **/
      @GET(value = "/users/sample3" )
    Observable<String>  sample2 (
          @Query(value = "ids" )  Long[] ids,
          @Query(value = "name" )  String name
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：数组
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：User
     **/
      @GET(value = "/users/sample2" )
    Observable<Map<String,User>>  sampleMap (
          @Query(value = "ids" )  Long[] ids,
          @Query(value = "name" )  String name,
          @QueryMap()  Sex sex,
          @Query(value = "testParam" )  Map<String,String[]>[] testParam
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：void
     **/
      @POST(value = "/users/uploadFile" )
    Observable<void>  uploadFile (
          @Field()  File multipartFile
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：Object
     **/
      @GET(value = "/users/test" )
    Observable<Map<String,Object>>  test3 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：PageInfo
      * 5:返回值在java中的类型为：User
     **/
      @GET(value = "/users/test2" )
    Observable<ServiceResponse<List<PageInfo<User>>>>  test4 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：User
     **/
      @GET(value = "/users/test5" )
    Observable<Map<String,List<PageInfo<User>>>>  test5 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Sex
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：User
     **/
      @GET(value = "/users/test6" )
    Observable<Map<Sex,List<PageInfo<User>>>>  test6 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Integer
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：User
     **/
      @GET(value = "/users/test7" )
    Observable<Map<Integer,List<PageInfo<User>>>>  test7 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：String
     **/
      @GET(value = "/users/test8" )
    Observable<Map<String,String>>  test8 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：数组
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：String
     **/
      @GET(value = "/users/test9" )
    Observable<Map<String,String>>  test9 (
          @Query(value = "id" )  Long id
  );
}
