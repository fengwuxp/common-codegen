package com.wuxp.codegen.swagger3.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

      import com.wuxp.codegen.swagger3.domain.Order;
      import com.wuxp.codegen.swagger3.resp.ServiceResponse;
      import com.wuxp.codegen.swagger3.resp.PageInfo;
      import java.util.Set;
      import com.wuxp.codegen.swagger3.evt.CreateOrderEvt;
      import com.wuxp.codegen.swagger3.evt.QueryOrderEvt;
      import java.util.List;
      import com.wuxp.codegen.swagger3.evt.ExampleDTO;
      import java.util.Map;

    /**
     * 接口：GET
    **/

  @FeignClient(
        path = "/order" 
  )
public interface OrderFeignClient{

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：List
      * 3:返回值在java中的类型为：Order
     **/
      @GetMapping()
    List<Order>  getOrder (
          @RequestParam(name = "names" ,required = true )  String[] names,
          @RequestHeader(name = "ids" ,required = true )  List<Integer> ids,
  Set<Order> moneys
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @GetMapping()
    PageInfo<Order>  queryOrder2 (
  QueryOrderEvt evt
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder (
          @RequestBody(required = true )  QueryOrderEvt evt
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder3 (
          @RequestBody(required = true )  QueryOrderEvt[] evt
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder4 (
          @RequestBody(required = true )  Set<QueryOrderEvt> evt
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder5 (
          @RequestBody(required = true )  Map<String,QueryOrderEvt> evt
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping()
    PageInfo<Order>  queryOrder6 (
          @RequestBody(required = true )  List<QueryOrderEvt> evt,
  Long memberId
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceQueryResponse
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping(produces = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    ServiceResponse<PageInfo<Order>>  queryOrder_2 (
          @RequestParam(name = "order_id" ,required = false )  Long oderId,
  String sn
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping()
    ServiceResponse<PageInfo<Order>>  queryPage (
  String id,
  Long memberId
  );
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Long
     **/
      @GetMapping()
    ServiceResponse<Long>  createOrder (
  CreateOrderEvt evt
  );
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Object
     **/
      @PostMapping()
    ServiceResponse<Object>  hello (
  );
    /**
      * 1:Http请求方法：DELETE
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Object
     **/
      @DeleteMapping(value = "/delete" )
    ServiceResponse<Object>  delete (
  ExampleDTO dto
  );
}
