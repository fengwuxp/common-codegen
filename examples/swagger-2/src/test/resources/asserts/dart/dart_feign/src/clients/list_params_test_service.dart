import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';

          import '../model/domain/order.dart';
          import '../model/domain/user.dart';


      /// list tst
      /// 接口：GET
      /// list test（源码注释）
@Feign
  @FeignClient(value:"/list",)
class ListParamsTestService extends FeignProxyClient {

ListParamsTestService() : super() {

}


      /// 1:POST /list
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：String
          @PostMapping()
  Future<String>  test1(
          @RequestBody()
        BuiltList<User> users,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("test1",
  [users,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:GET /list/test_2
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：String
          @GetMapping(value:"test_2",)
  Future<String>  test2(
        BuiltList<User> users,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("test2",
  [users,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:GET /list/test_3
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：String
          @GetMapping(value:"test_3",)
  Future<String>  test3(
        BuiltMap<String,Order> users,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("test3",
  [users,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:POST /list/test_4
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：String
          @PostMapping(value:"test_4",)
  Future<String>  test4(
          @RequestBody()
        BuiltSet<User> users,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("test4",
  [users,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:POST /list/test_5
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：String
          @PostMapping(value:"test_5",)
  Future<String>  test5(
          @RequestBody()
        BuiltList<User> users,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("test5",
  [users,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:POST /list/test_6
      /// 2:Http请求方法：POST
      /// 3:返回值在java中的类型为：String
          @PostMapping(value:"test_6",)
  Future<String>  test6(
          @RequestBody()
        BuiltSet<User> users,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("test6",
  [users,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }
}


final listParamsTestService = ListParamsTestService();
