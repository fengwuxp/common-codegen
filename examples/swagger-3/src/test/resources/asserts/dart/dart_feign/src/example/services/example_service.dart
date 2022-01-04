import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';

          import '../../example_dto.dart';
          import '../../evt/example_dto.dart';
          import '../../serializers.dart';


      /// 接口：GET
      /// example_cms
@Feign
  @FeignClient(value:"/example_cms",)
class ExampleService extends FeignProxyClient {

ExampleService() : super() {

}


      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：List
      /// 3:返回值在java中的类型为：Integer
          @GetMapping(value:"get_num",)
  Future<BuiltList<int>>  getNums(
        int num,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltList<int>>("getNums",
  [num,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltList,[FullType(int)])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：List
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：Integer
      /// 5:返回值在java中的类型为：String
          @GetMapping(value:"get_maps",)
  Future<BuiltList<BuiltMap<int,String>>>  getMaps(
        int num,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltList<BuiltMap<int,String>>>("getMaps",
  [num,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltList,[FullType(BuiltMap,[FullType(int),FullType(String)])])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：String
      /// 4:返回值在java中的类型为：Integer
          @GetMapping(value:"get_map",)
  Future<BuiltMap<String,int>>  getMap(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,int>>("getMap",
  [],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(int)])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：String
      /// 4:返回值在java中的类型为：List
      /// 5:返回值在java中的类型为：Boolean
          @GetMapping(value:"get_map_2",)
  Future<BuiltMap<String,BuiltList<bool>>>  getMap2(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,BuiltList<bool>>>("getMap2",
  [],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(bool)])])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：String
      /// 4:返回值在java中的类型为：List
      /// 5:返回值在java中的类型为：Boolean
          @GetMapping(value:"get_map_3/{test_id}",)
  Future<BuiltMap<String,BuiltList<bool>>>  getMap3(
          @PathVariable(name: "test_id" ,required: true )
        String id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,BuiltList<bool>>>("getMap3",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(bool)])])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：String
      /// 4:返回值在java中的类型为：List
      /// 5:返回值在java中的类型为：Boolean
          @GetMapping(value:"get_map_4/{test_id}",)
  Future<BuiltMap<String,BuiltList<bool>>>  getMap4(
        ExampleDTO dto,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,BuiltList<bool>>>("getMap4",
  [dto,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(bool)])])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：String
      /// 4:返回值在java中的类型为：List
      /// 5:返回值在java中的类型为：Boolean
          @GetMapping(value:"get_map_5/{test_id}",)
  Future<BuiltMap<String,BuiltList<bool>>>  getMap5(
        ExampleDto dto,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,BuiltList<bool>>>("getMap5",
  [dto,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(bool)])])
          )
    
  );
  }
}


final exampleService = ExampleService();
