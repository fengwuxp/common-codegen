import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';

          import '../model/domain/order.dart';
          import '../model/evt/query_order_evt.dart';
          import '../model/evt/create_order_evt.dart';
          import '../enums/sex.dart';
          import '../model/paging/Page.dart';
          import '../model/resp/page_info.dart';


      /// 订单服务
      /// 接口：GET
@Feign
  @FeignClient(value:"/order",)
class OrderService extends FeignProxyClient {

OrderService() : super() {

}


      /// 1:GET /order/get_order
      /// 2:获取订单列表
      /// 3:Http请求方法：GET
      /// 4:返回值在java中的类型为：List
      /// 5:返回值在java中的类型为：Order
          @GetMapping(value:"get_order",headers:{"names":"{names}"},)
  Future<BuiltList<Order>>  getOrder(
          @RequestHeader(name: "names" )
        BuiltList<String> names,
          @CookieValue(name: "my_ids" )
        BuiltList<int> ids,
        BuiltSet<Order> moneys,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltList<Order>>("getOrder",
  [names,ids,moneys,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltList,[FullType(Order)])
          )
    
  );
  }

      /// 1:GET /order/get_order_32
      /// 2:获取订单列表
      /// 3:Http请求方法：GET
      /// 4:返回值在java中的类型为：List
      /// 5:返回值在java中的类型为：Order
          @GetMapping(value:"get_order_32",)
  Future<BuiltList<Order>>  getOrder32(
          @RequestParam(name: "names" )
        BuiltList<String> names,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltList<Order>>("getOrder32",
  [names,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltList,[FullType(Order)])
          )
    
  );
  }

      /// 1:GET /order/queryOrder
      /// 2:获取订单列表
      /// 3:Http请求方法：GET
      /// 4:返回值在java中的类型为：PageInfo
      /// 5:返回值在java中的类型为：Order
          @GetMapping(value:"/queryOrder",headers:{"X-User-Id":"{userId}"},)
  Future<PageInfo<Order>>  queryOrder(
          @RequestHeader(name: "X-User-Id" )
        String userId,
        QueryOrderEvt evt,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryOrder",
  [userId,evt,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:GET /order
      /// 2:获取订单列表
      /// 3:Http请求方法：GET
      /// 4:返回值在java中的类型为：Page
      /// 5:返回值在java中的类型为：Order
          @GetMapping()
  Future<Page<Order>>  pageBySpringData(
        QueryOrderEvt evt,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<Page<Order>>("pageBySpringData",
  [evt,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: Page,
              specifiedType:FullType(Page,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order/queryOrder2
      /// 2:获取订单列表
      /// 3:Http请求方法：POST
      /// 4:返回值在java中的类型为：ServiceQueryResponse
      /// 5:返回值在java中的类型为：Order
          @PostMapping(produces:[HttpMediaType.MULTIPART_FORM_DATA],)
  Future<PageInfo<Order>>  queryOrder2(
          @RequestParam(name: "order_id" ,required: false )
        int oderId,
        String sn,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryOrder2",
  [oderId,sn,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order/queryPage
      /// 2:查询分页
      /// 3:Http请求方法：POST
      /// <pre>
      /// 5:参数列表：
      /// 6:参数名称：id，参数说明：null
      /// </pre>
      /// 8:返回值在java中的类型为：ServiceResponse
      /// 9:返回值在java中的类型为：PageInfo
      /// 10:返回值在java中的类型为：Order
          @PostMapping()
  Future<PageInfo<Order>>  queryPage(
        String id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryPage",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order/createOrder
      /// 2:创建订单
      /// 3:Http请求方法：POST
      /// <pre>
      /// 5:参数列表：
      /// 6:参数名称：evt，参数说明：null
      /// </pre>
      /// 8:返回值在java中的类型为：ServiceResponse
      /// 9:返回值在java中的类型为：Long
          @PostMapping()
  Future<int>  createOrder(
          @RequestBody()
        CreateOrderEvt evt,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<int>("createOrder",
  [evt,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(int)
          )
    
  );
  }

      /// 1:POST /order/hello
      /// 2:test hello
      /// 3:Http请求方法：POST
      /// 4:返回值在java中的类型为：ServiceResponse
      /// 5:返回值在java中的类型为：Object
          @PostMapping()
  Future<Object>  hello(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<Object>("hello",
  [],
    feignOptions: feignOptions
  );
  }

      /// 1:POST /order/hello_2
      /// 2:test hello
      /// 3:Http请求方法：POST
      /// 4:返回值在java中的类型为：ServiceQueryResponse
      /// 5:返回值在java中的类型为：Object
          @PostMapping(value:"hello_2",)
  Future<PageInfo<Object>>  hello2(
          @RequestParam(defaultValue: "test" ,name: "name" ,required: false )
        String name,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Object>>("hello2",
  [name,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Object)])
          )
    
  );
  }

      /// 1:POST /order/hello_3
      /// 2:test hello_3
      /// 3:Http请求方法：POST
      /// 4:返回值在java中的类型为：ServiceQueryResponse
      /// 5:返回值在java中的类型为：String
          @PostMapping(value:"hello_3",)
  Future<PageInfo<String>>  hello3(
          @RequestParam(defaultValue: "test" ,name: "name" ,required: false )
        String name,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<String>>("hello3",
  [name,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(String)])
          )
    
  );
  }

      /// 1:DELETE /order/hello_delete
      /// 2:test hello
      /// 3:Http请求方法：DELETE
      /// 4:返回值在java中的类型为：void
          @DeleteMapping(value:"hello_delete",)
  Future<void>  delete(
          @RequestParam(name: "id" ,required: false )
        String id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<void>("delete",
  [id,],
    feignOptions: feignOptions
  );
  }

      /// 1:GET /order/testEnumNames
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：Sex
      /// 5:返回值在java中的类型为：Sex
          @GetMapping(value:"/testEnumNames",)
  Future<BuiltMap<Sex,Sex>>  testEnumNames(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<Sex,Sex>>("testEnumNames",
  [],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(Sex),FullType(Sex)])
          )
    
  );
  }

      /// 1:GET /order/testEnumNames2
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：String
      /// 5:返回值在java中的类型为：Sex
          @GetMapping(value:"/testEnumNames2",)
  Future<BuiltMap<String,Sex>>  testEnumNames2(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,Sex>>("testEnumNames2",
  [],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(Sex)])
          )
    
  );
  }

      /// 1:GET /order/testEnumNames3
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：Sex
      /// 5:返回值在java中的类型为：Integer
          @GetMapping(value:"/testEnumNames3",)
  Future<BuiltMap<Sex,int>>  testEnumNames3(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<Sex,int>>("testEnumNames3",
  [],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(Sex),FullType(int)])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：void
          @GetMapping(value:"/test2",)
  Future<void>  test2(
        Object t,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<void>("test2",
  [t,],
    feignOptions: feignOptions
  );
  }
}


final orderService = OrderService();
