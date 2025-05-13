import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';



      /// 接口：GET
@Feign
  @FeignClient(value:"/hello",)
class HelloService extends FeignProxyClient {

HelloService() : super() {

}


      /// 1:GET /hello/hello
      /// 2:Http请求方法：GET
      /// 3:Documented with OpenAPI v3 annotations
      /// 4:标记忽略
      /// 5:返回值在java中的类型为：String
          @GetMapping(value:"/hello",)
  Future<String>  index(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("index",
  [],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }
}


final helloService = HelloService();
