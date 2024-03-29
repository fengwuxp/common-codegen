package com.wuxp.codegen.swagger2.clients;
import retrofit2.http.*;


      import java.util.Set;
      import java.util.Collection;
      import java.util.List;
      import java.util.Map;

    /**
     * list tst
     * 接口：GET
     * list test（源码注释）
    **/

public interface ListParamsTestRetrofitClient{

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @POST(value = "/list" )
    Observable<String>  test1 (
          @Body()  List<User> users
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GET(value = "/list/test_2" )
    Observable<String>  test2 (
  User[] users
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GET(value = "/list/test_3" )
    Observable<String>  test3 (
  Map<String,Order> users
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @POST(value = "/list/test_4" )
    Observable<String>  test4 (
          @Body()  Set<User> users
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @POST(value = "/list/test_5" )
    Observable<String>  test5 (
          @Body()  Collection<User> users
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @POST(value = "/list/test_6" )
    Observable<String>  test6 (
          @Body()  Set<User> users
  );
}
