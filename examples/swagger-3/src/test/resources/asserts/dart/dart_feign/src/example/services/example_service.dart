import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';

          import '../../domain/user.dart';
          import '../../domain/base_info.dart';
          import '../../resp/example_dto.dart';


      /// 接口：GET
      /// example_cms
@Feign
  @FeignClient(value:"/example_cms",)
class ExampleService extends FeignProxyClient {

ExampleService() : super() {

}


      /// 1:GET /example_cms/get_num
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：List
      /// 4:返回值在java中的类型为：Integer
          @GetMapping(value:"get_num",)
  Future<BuiltList<int>>  getNums(
          @RequestParam(name: "num" )
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

      /// 1:GET /example_cms/get_maps
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：List
      /// 4:返回值在java中的类型为：Map
      /// 5:返回值在java中的类型为：Integer
      /// 6:返回值在java中的类型为：String
          @GetMapping(value:"get_maps",)
  Future<BuiltList<BuiltMap<int,String>>>  getMaps(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltList<BuiltMap<int,String>>>("getMaps",
  [],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltList,[FullType(BuiltMap,[FullType(int),FullType(String)])])
          )
    
  );
  }

      /// 1:GET /example_cms/get_map
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：String
      /// 5:返回值在java中的类型为：Integer
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

      /// 1:GET /example_cms/get_map_2
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：String
      /// 5:返回值在java中的类型为：List
      /// 6:返回值在java中的类型为：Boolean
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

      /// 1:GET /example_cms/get_map_3/{id}
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：String
      /// 5:返回值在java中的类型为：List
      /// 6:返回值在java中的类型为：Boolean
          @GetMapping(value:"get_map_3/{id}",)
  Future<BuiltMap<String,BuiltList<bool>>>  getMap3(
          @PathVariable(name: "id" )
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

      /// 1:GET /example_cms/get_map_/
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：String
      /// 5:返回值在java中的类型为：List
      /// 6:返回值在java中的类型为：Boolean
          @GetMapping(value:"get_map_/",)
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

      /// 1:GET /example_cms/get_map_5
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：String
      /// 5:返回值在java中的类型为：List
      /// 6:返回值在java中的类型为：Boolean
          @GetMapping(value:"get_map_5",)
  Future<BuiltMap<String,BuiltList<bool>>>  getMap5(
        ExampleDTO dto,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,BuiltList<bool>>>("getMap5",
  [dto,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(bool)])])
          )
    
  );
  }

      /// 1:GET /example_cms/get_map_6
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：void
          @GetMapping(value:"get_map_6",)
  Future<void>  example0(
        BaseInfo<String,User> req,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<void>("example0",
  [req,],
    feignOptions: feignOptions
  );
  }

      /// 1:GET /example_cms/get_map_7
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：void
          @GetMapping(value:"get_map_7",)
  Future<void>  example2(
        BuiltMap<int,User> req,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<void>("example2",
  [req,],
    feignOptions: feignOptions
  );
  }

      /// 1:GET /example_cms/get_map_8
      /// 2:Http请求方法：GET
      /// 3:返回值在java中的类型为：void
          @GetMapping(value:"get_map_8",)
  Future<void>  example3(
        BuiltList<User> req,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<void>("example3",
  [req,],
    feignOptions: feignOptions
  );
  }
}


final exampleService = ExampleService();
