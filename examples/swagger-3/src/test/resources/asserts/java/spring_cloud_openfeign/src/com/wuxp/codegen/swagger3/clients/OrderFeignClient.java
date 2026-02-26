package com.wuxp.codegen.swagger3.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

      import com.wuxp.codegen.swagger3.domain.Order;
      import java.util.Set;
      import com.wuxp.codegen.swagger3.evt.QueryOrderEvt;
      import com.wuxp.codegen.swagger3.evt.CreateOrderEvt;
      import com.wuxp.codegen.swagger3.resp.ExampleDTO;
      import com.wuxp.codegen.swagger3.resp.PageInfo;
      import com.wuxp.codegen.swagger3.resp.ServiceResponse;
      import java.util.List;
      import java.util.Map;

    /**
     * 接口：GET
    **/

  @FeignClient(
        path = "/order" 
  )
public interface OrderFeignClient{

    /**
      * 1:GET /order/getOrder
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Order
     **/
      @GetMapping()
    List<Order>  getOrder (
          @RequestParam(name = "names" )  String[] names,
          @RequestHeader(name = "My-Ids" )  List<Integer> ids,
  Set<Order> moneys
  );
    /**
      * 1:GET /order
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @GetMapping()
    PageInfo<Order>  queryOrder2 (
  QueryOrderEvt evt
  );
    /**
      * 1:POST /order
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder (
          @RequestBody()  QueryOrderEvt evt
  );
    /**
      * 1:POST /order/queryOrder3
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder3 (
          @RequestBody()  QueryOrderEvt[] evt
  );
    /**
      * 1:POST /order/queryOrder4
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder4 (
          @RequestBody()  Set<QueryOrderEvt> evt
  );
    /**
      * 1:POST /order/queryOrder5
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder5 (
          @RequestBody()  Map<String,QueryOrderEvt> evt
  );
    /**
      * 1:POST /order/queryOrder6
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder6 (
          @RequestBody()  List<QueryOrderEvt> evt
  );
    /**
      * 1:POST /order/queryOrder_2
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping(produces = {[HttpMediaType.MULTIPART_FORM_DATA]} )
    ServiceResponse<PageInfo<Order>>  queryOrder_2 (
          @RequestParam(name = "order_id" ,required = false )  Long oderId,
  String sn
  );
    /**
      * 1:POST /order/queryPage
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：PageInfo
      * 5:返回值在java中的类型为：Order
     **/
      @PostMapping()
    ServiceResponse<PageInfo<Order>>  queryPage (
  String id
  );
    /**
      * 1:GET /order/createOrder
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：Long
     **/
      @GetMapping()
    ServiceResponse<Long>  createOrder (
  CreateOrderEvt evt
  );
    /**
      * 1:POST /order/hello
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：Object
     **/
      @PostMapping()
    ServiceResponse<Object>  hello (
  );
    /**
      * 1:DELETE /order/delete
      * 2:Http请求方法：DELETE
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：Object
     **/
      @DeleteMapping(value = "/delete" )
    ServiceResponse<Object>  delete (
  ExampleDTO dto
  );
}
