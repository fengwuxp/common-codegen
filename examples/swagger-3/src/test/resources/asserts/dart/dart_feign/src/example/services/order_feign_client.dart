import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';

          import '../../domain/order.dart';
          import '../../resp/page_info.dart';
          import '../../evt/query_order_evt.dart';
          import '../../evt/create_order_evt.dart';
          import '../../evt/example_dto.dart';
          import '../../serializers.dart';


      /// 接口：GET
@Feign
  @FeignClient(value:"/order",)
class OrderFeignClient extends FeignProxyClient {

OrderFeignClient() : super() {

}


      /// 1:GET /order/getOrder
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：List
      /// 4:返回值在java中的类型为：Order
          @GetMapping(headers:{"My-Ids":"{ids}"},)
  Future<BuiltList<Order>>  getOrder(
          @RequestParam(name: "names" )
        BuiltList<String> names,
          @RequestHeader(name: "My-Ids" )
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

      /// 1:GET /order
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：PageInfo
      /// 4:返回值在java中的类型为：Order
          @GetMapping()
  Future<PageInfo<Order>>  queryOrder2(
        QueryOrderEvt evt,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryOrder2",
  [evt,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：PageInfo
      /// 4:返回值在java中的类型为：Order
          @PostMapping()
  Future<PageInfo<Order>>  queryOrder(
          @RequestBody()
        QueryOrderEvt evt,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryOrder",
  [evt,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order/queryOrder3
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：PageInfo
      /// 4:返回值在java中的类型为：Order
          @PostMapping()
  Future<PageInfo<Order>>  queryOrder3(
          @RequestBody()
        BuiltList<QueryOrderEvt> evt,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryOrder3",
  [evt,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order/queryOrder4
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：PageInfo
      /// 4:返回值在java中的类型为：Order
          @PostMapping()
  Future<PageInfo<Order>>  queryOrder4(
          @RequestBody()
        BuiltSet<QueryOrderEvt> evt,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryOrder4",
  [evt,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order/queryOrder5
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：PageInfo
      /// 4:返回值在java中的类型为：Order
          @PostMapping()
  Future<PageInfo<Order>>  queryOrder5(
          @RequestBody()
        BuiltMap<String,QueryOrderEvt> evt,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryOrder5",
  [evt,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order/queryOrder6
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：PageInfo
      /// 4:返回值在java中的类型为：Order
          @PostMapping()
  Future<PageInfo<Order>>  queryOrder6(
          @RequestBody()
        BuiltList<QueryOrderEvt> evt,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryOrder6",
  [evt,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order/queryOrder_2
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：ServiceQueryResponse
      /// 4:返回值在java中的类型为：Order
          @PostMapping(produces:[HttpMediaType.MULTIPART_FORM_DATA],)
  Future<PageInfo<Order>>  queryOrder_2(
          @RequestParam(name: "order_id" ,required: false )
        int oderId,
        String sn,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<PageInfo<Order>>("queryOrder_2",
  [oderId,sn,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: PageInfo,
              specifiedType:FullType(PageInfo,[FullType(Order)])
          )
    
  );
  }

      /// 1:POST /order/queryPage
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：ServiceResponse
      /// 4:返回值在java中的类型为：PageInfo
      /// 5:返回值在java中的类型为：Order
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

      /// 1:GET /order/createOrder
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：ServiceResponse
      /// 4:返回值在java中的类型为：Long
          @GetMapping()
  Future<int>  createOrder(
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
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：ServiceResponse
      /// 4:返回值在java中的类型为：Object
          @PostMapping()
  Future<Object>  hello(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<Object>("hello",
  [],
    feignOptions: feignOptions
  );
  }

      /// 1:DELETE /order/delete
      /// 2:Http请求方法：DELETE
      /// 3:返回值在java中的类型为：ServiceResponse
      /// 4:返回值在java中的类型为：Object
          @DeleteMapping(value:"/delete",)
  Future<Object>  delete(
        ExampleDTO dto,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<Object>("delete",
  [dto,],
    feignOptions: feignOptions
  );
  }
}


final orderFeignClient = OrderFeignClient();
