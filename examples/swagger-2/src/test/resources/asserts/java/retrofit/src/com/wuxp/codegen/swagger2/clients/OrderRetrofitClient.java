package com.wuxp.codegen.swagger2.clients;
import retrofit2.http.*;


      import com.wuxp.codegen.swagger2.model.domain.Order;
      import java.util.Set;
      import com.wuxp.codegen.swagger2.model.evt.CreateOrderEvt;
      import com.wuxp.codegen.swagger2.model.evt.QueryOrderEvt;
      import com.wuxp.codegen.swagger2.enums.Sex;
      import com.wuxp.codegen.swagger2.model.paging.Page;
      import com.wuxp.codegen.swagger2.model.resp.ServiceResponse;
      import com.wuxp.codegen.swagger2.model.resp.PageInfo;
      import java.util.List;
      import java.util.Map;

    /**
     * 订单服务
     * 接口：GET
    **/

public interface OrderRetrofitClient{

    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Order
     **/
      @GET(value = "/order/get_order" )
    List<Order>  getOrder (
  String text,
          @Header(value = "names" )  String[] names,
          @Header(value = cookie@"my_ids" )  List<Integer> ids,
  Set<Order> moneys
  );
    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Order
     **/
      @GET(value = "/order/get_order_32" )
    List<Order>  getOrder32 (
          @Query(value = "names" )  String[] names
  );
    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @GET(value = "/order/queryOrder" )
    PageInfo<Order>  queryOrder (
          @Header(value = "X-User-Id" )  String userId,
  QueryOrderEvt evt
  );
    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Page
      * 4:返回值在java中的类型为：Order
     **/
      @GET(value = "/order" )
    Page<Order>  pageBySpringData (
  QueryOrderEvt evt
  );
    /**
      * 1:获取订单列表
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：Order
     **/
      @POST(value = "/order/queryOrder2" )
      @Headers(value = {"Content-Type: application/json"} )
    ServiceResponse<PageInfo<Order>>  queryOrder2 (
          @Field(value = "order_id" )  Long oderId,
  String sn,
          @Header(value = cookie@"memberId" )  Long memberId
  );
    /**
      * 1:查询分页
      * 2:Http请求方法：POST
      * <pre>
      * 4:参数列表：
      * 5:参数名称：id，参数说明：null
      * </pre>
      * 7:返回值在java中的类型为：ServiceResponse
      * 8:返回值在java中的类型为：PageInfo
      * 9:返回值在java中的类型为：Order
     **/
      @POST(value = "/order/queryPage" )
      @Headers(value = {"Content-Type: application/json"} )
    ServiceResponse<PageInfo<Order>>  queryPage (
  String id
  );
    /**
      * 1:创建订单
      * 2:Http请求方法：POST
      * <pre>
      * 4:参数列表：
      * 5:参数名称：evt，参数说明：null
      * </pre>
      * 7:返回值在java中的类型为：ServiceResponse
      * 8:返回值在java中的类型为：Long
     **/
      @POST(value = "/order/createOrder" )
    ServiceResponse<Long>  createOrder (
          @Body()  CreateOrderEvt evt
  );
    /**
      * 1:test hello
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：Object
     **/
      @POST(value = "/order/hello" )
    ServiceResponse<Object>  hello (
  );
    /**
      * 1:test hello
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：Object
     **/
      @POST(value = "/order/hello_2" )
    ServiceResponse<PageInfo<Object>>  hello2 (
          @Field(defaultValue = "test" ,value = "name" )  String name
  );
    /**
      * 1:test hello_3
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：String
     **/
      @POST(value = "/order/hello_3" )
    ServiceResponse<PageInfo<String>>  hello3 (
          @Field(defaultValue = "test" ,value = "name" )  String name
  );
    /**
      * 1:test hello
      * 2:Http请求方法：DELETE
      * 3:返回值在java中的类型为：void
     **/
      @DELETE(value = "/order/hello_delete" )
    void  delete (
          @Query(value = "id" )  String id
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Sex
      * 4:返回值在java中的类型为：Sex
     **/
      @GET(value = "/order/testEnumNames" )
    Map<Sex,Sex>  testEnumNames (
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：Sex
     **/
      @GET(value = "/order/testEnumNames2" )
    Map<String,Sex>  testEnumNames2 (
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Sex
      * 4:返回值在java中的类型为：Integer
     **/
      @GET(value = "/order/testEnumNames3" )
    Map<Sex,Integer>  testEnumNames3 (
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：void
     **/
      @GET(value = "/test2" )
    void  test2 (
  Object t
  );
}
