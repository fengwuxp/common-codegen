package com.wuxp.codegen.swagger3.retrofits;
import retrofit2.http.*;


      import com.wuxp.codegen.swagger3.domain.Order;
      import com.wuxp.codegen.swagger3.domain.User;
      import com.wuxp.codegen.swagger3.enums.Sex;
      import com.wuxp.codegen.swagger3.resp.ServiceResponse;
      import com.wuxp.codegen.swagger3.resp.PageInfo;
      import java.util.List;
      import java.util.Map;
      import java.io.File;

    /**
     * 接口：GET
     * user
     * 通过这里配置使下面的映射都在/users下，可去除
    **/

public interface UserService{

    /**
      * 1:GET /users/{id}
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：User
     **/
      @GET(value = "/users/{id}" )
    User  getUser (
          @Path(value = "id" )  Long id
  );
    /**
      * 1:PUT /users/{id}
      * 2:Http请求方法：PUT
      * 3:返回值在java中的类型为：String
     **/
      @PUT(value = "/users/{id}" )
    String  putUser (
          @Path(value = "id" )  Long id,
          @Body()  User user,
          @Body()  Order order
  );
    /**
      * 1:DELETE /users/{id}
      * 2:Http请求方法：DELETE
      * 3:返回值在java中的类型为：String
     **/
      @DELETE(value = "/users/{id}" )
    String  deleteUser (
          @Path(value = "id" )  Long id,
          @Query(value = "name" )  String name
  );
    /**
      * 1:GET /users/sample
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/
      @GET(value = "/users/sample" )
    String  sample (
          @Query(value = "ids" )  Long[] ids,
          @Query(value = "name" )  String name
  );
    /**
      * 1:GET /users/sample3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：String
     **/
      @GET(value = "/users/sample3" )
    String  sample2 (
          @Query(value = "ids" )  Long[] ids,
          @Query(value = "name" )  String name
  );
    /**
      * 1:GET /users/sample2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：User
     **/
      @GET(value = "/users/sample2" )
    Map<String,User>  sampleMap (
          @Query(value = "ids" )  Long[] ids,
          @Query(value = "name" )  String name,
          @Query(value = "sex" )  Sex sex,
          @Query(value = "testParam" )  Map<String,String[]>[] testParam
  );
    /**
      * 1:POST /users/uploadFile
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：void
     **/
      @POST(value = "/users/uploadFile" )
    void  uploadFile (
          @Query(value = "file" )  File multipartFile
  );
    /**
      * 1:GET /users/test
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Object
     **/
      @GET(value = "/users/test" )
    Map<String,Object>  test3 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:GET /users/test2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：User
     **/
      @GET(value = "/users/test2" )
    ServiceResponse<List<PageInfo<User>>>  test4 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:GET /users/test5
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：User
     **/
      @GET(value = "/users/test5" )
    Map<String,List<PageInfo<User>>>  test5 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:GET /users/test6
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：User
     **/
      @GET(value = "/users/test6" )
    Map<Sex,List<PageInfo<User>>>  test6 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:GET /users/test7
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Integer
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：User
     **/
      @GET(value = "/users/test7" )
    Map<Integer,List<PageInfo<User>>>  test7 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:GET /users/test8
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：String
     **/
      @GET(value = "/users/test8" )
    Map<String,String>  test8 (
          @Query(value = "id" )  Long id
  );
    /**
      * 1:GET /users/test9
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：Map
      * 7:返回值在java中的类型为：String
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：String
     **/
      @GET(value = "/users/test9" )
    Map<String,String>  test9 (
          @Query(value = "id" )  Long id
  );
}
