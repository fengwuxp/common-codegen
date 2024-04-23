package com.wuxp.codegen.swagger2.clients;
import retrofit2.http.*;


      import com.wuxp.codegen.swagger2.model.domain.Order;
      import java.util.Set;
      import com.wuxp.codegen.swagger2.model.domain.User;
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
    String  test1 (
          @Body()  List<User> users
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GET(value = "/list/test_2" )
    String  test2 (
  User[] users
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GET(value = "/list/test_3" )
    String  test3 (
  Map<String,Order> users
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @POST(value = "/list/test_4" )
    String  test4 (
          @Body()  Set<User> users
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @POST(value = "/list/test_5" )
    String  test5 (
          @Body()  Collection<User> users
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @POST(value = "/list/test_6" )
    String  test6 (
          @Body()  Set<User> users
  );
}
