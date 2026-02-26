package com.wuxp.codegen.swagger2.clients;

import org.springframework.web.service.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import com.wind.client.rest.annotation.SpringQueryMap;

      import com.wuxp.codegen.swagger2.domain.Order;
      import java.util.Set;
      import com.wuxp.codegen.swagger2.evt.QueryOrderEvt;
      import com.wuxp.codegen.swagger2.evt.CreateOrderEvt;
      import com.wuxp.codegen.swagger2.enums.Sex;
      import com.wuxp.codegen.swagger2.model.paging.Page;
      import com.wuxp.codegen.swagger2.resp.PageInfo;
      import com.wuxp.codegen.swagger2.resp.ServiceResponse;
      import java.util.List;
      import java.util.Map;

    /**
     * 订单服务
     * 接口：GET
    **/

  @HttpExchange(
        value = "/order" 
  )
public interface OrderClient{

    /**
      * 1:GET /order/get_order
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Order
     **/
      @GetExchange(value = "get_order" )
    List<Order>  getOrder (
          @RequestHeader(name = "names" )  String[] names,
          @CookieValue(name = "my_ids" )  List<Integer> ids,
  Set<Order> moneys
  );
    /**
      * 1:GET /order/get_order_32
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Order
     **/
      @GetExchange(value = "get_order_32" )
    List<Order>  getOrder32 (
          @RequestParam(name = "names" )  String[] names
  );
    /**
      * 1:GET /order/queryOrder
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：PageInfo
      * 5:返回值在java中的类型为：Order
     **/
      @GetExchange(value = "/queryOrder" )
    PageInfo<Order>  queryOrder (
          @RequestHeader(name = "X-User-Id" )  String userId,
          @SpringQueryMap()  QueryOrderEvt evt
  );
    /**
      * 1:GET /order
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Page
      * 5:返回值在java中的类型为：Order
     **/
      @GetExchange()
    Page<Order>  pageBySpringData (
          @SpringQueryMap()  QueryOrderEvt evt
  );
    /**
      * 1:POST /order/queryOrder2
      * 2:获取订单列表
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：Order
     **/
      @PostExchange(accept = {[HttpMediaType.MULTIPART_FORM_DATA]} )
    ServiceResponse<PageInfo<Order>>  queryOrder2 (
          @RequestParam(name = "order_id" ,required = false )  Long oderId,
  String sn
  );
    /**
      * 1:POST /order/queryPage
      * 2:查询分页
      * 3:Http请求方法：POST
      * <pre>
      * 5:参数列表：
      * 6:参数名称：id，参数说明：null
      * </pre>
      * 8:返回值在java中的类型为：ServiceResponse
      * 9:返回值在java中的类型为：PageInfo
      * 10:返回值在java中的类型为：Order
     **/
      @PostExchange()
    ServiceResponse<PageInfo<Order>>  queryPage (
  String id
  );
    /**
      * 1:POST /order/createOrder
      * 2:创建订单
      * 3:Http请求方法：POST
      * <pre>
      * 5:参数列表：
      * 6:参数名称：evt，参数说明：null
      * </pre>
      * 8:返回值在java中的类型为：ServiceResponse
      * 9:返回值在java中的类型为：Long
     **/
      @PostExchange()
    ServiceResponse<Long>  createOrder (
          @RequestBody()  CreateOrderEvt evt
  );
    /**
      * 1:POST /order/hello
      * 2:test hello
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceResponse
      * 5:返回值在java中的类型为：Object
     **/
      @PostExchange()
    ServiceResponse<Object>  hello (
  );
    /**
      * 1:POST /order/hello_2
      * 2:test hello
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：Object
     **/
      @PostExchange(value = "hello_2" )
    ServiceResponse<PageInfo<Object>>  hello2 (
          @RequestParam(defaultValue = "test" ,name = "name" ,required = false )  String name
  );
    /**
      * 1:POST /order/hello_3
      * 2:test hello_3
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：String
     **/
      @PostExchange(value = "hello_3" )
    ServiceResponse<PageInfo<String>>  hello3 (
          @RequestParam(defaultValue = "test" ,name = "name" ,required = false )  String name
  );
    /**
      * 1:DELETE /order/hello_delete
      * 2:test hello
      * 3:Http请求方法：DELETE
      * 4:返回值在java中的类型为：void
     **/
      @DeleteExchange(value = "hello_delete" )
    void  delete (
          @RequestParam(name = "id" ,required = false )  String id
  );
    /**
      * 1:GET /order/testEnumNames
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：Sex
     **/
      @GetExchange(value = "/testEnumNames" )
    Map<Sex,Sex>  testEnumNames (
  );
    /**
      * 1:GET /order/testEnumNames2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Sex
     **/
      @GetExchange(value = "/testEnumNames2" )
    Map<String,Sex>  testEnumNames2 (
  );
    /**
      * 1:GET /order/testEnumNames3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：Integer
     **/
      @GetExchange(value = "/testEnumNames3" )
    Map<Sex,Integer>  testEnumNames3 (
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：void
     **/
      @GetExchange(value = "/test2" )
    void  test2 (
  Object t
  );
}
