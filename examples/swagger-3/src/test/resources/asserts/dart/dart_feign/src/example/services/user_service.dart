import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';

          import '../../domain/order.dart';
          import '../../resp/page_info.dart';
          import '../../domain/user.dart';
          import '../../enums/sex.dart';
          import '../../serializers.dart';


      /// 接口：GET
      /// user
      /// 通过这里配置使下面的映射都在/users下，可去除
@Feign
  @FeignClient(value:"/users",)
class UserService extends FeignProxyClient {

UserService() : super() {

}


      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：User
          @GetMapping(value:"/{id}",)
  Future<User>  getUser(
          @PathVariable(name: "id" )
        int id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<User>("getUser",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: User,
          )
    
  );
  }

      /// 1:Http请求方法：PUT
      /// 2:返回值在java中的类型为：String
          @PutMapping(value:"/{id}",)
  Future<String>  putUser(
          @PathVariable(name: "id" )
        int id,
          @RequestBody()
        User user,
          @RequestBody()
        Order order,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("putUser",
  [id,user,order,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:Http请求方法：DELETE
      /// 2:返回值在java中的类型为：String
          @DeleteMapping(value:"/{id}",)
  Future<String>  deleteUser(
          @PathVariable(name: "id" )
        int id,
        String name,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("deleteUser",
  [id,name,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：String
          @GetMapping()
  Future<String>  sample(
        BuiltList<int> ids,
        String name,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("sample",
  [ids,name,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：数组
      /// 3:返回值在java中的类型为：String
          @GetMapping(value:"sample3",)
  Future<String>  sample2(
        BuiltList<int> ids,
        String name,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("sample2",
  [ids,name,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：数组
      /// 3:返回值在java中的类型为：Map
      /// 4:返回值在java中的类型为：String
      /// 5:返回值在java中的类型为：数组
      /// 6:返回值在java中的类型为：User
          @GetMapping(value:"sample2",)
  Future<BuiltMap<String,User>>  sampleMap(
        BuiltList<int> ids,
        String name,
        Sex sex,
        BuiltList<BuiltMap<String,BuiltList<String>>> testParam,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,User>>("sampleMap",
  [ids,name,sex,testParam,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(User)])
          )
    
  );
  }

      /// 1:Http请求方法：POST
      /// 2:返回值在java中的类型为：void
          @PostMapping(produces:[HttpMediaType.MULTIPART_FORM_DATA],)
  Future<void>  uploadFile(
          @RequestParam(name: "file" )
        File multipartFile,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<void>("uploadFile",
  [multipartFile,],
    feignOptions: feignOptions
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：String
      /// 4:返回值在java中的类型为：Object
          @GetMapping(value:"/test",)
  Future<BuiltMap<String,Object>>  test3(
        int id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,Object>>("test3",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(Object)])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：ServiceResponse
      /// 3:返回值在java中的类型为：List
      /// 4:返回值在java中的类型为：PageInfo
      /// 5:返回值在java中的类型为：User
          @GetMapping(value:"/test2",)
  Future<BuiltList<PageInfo<User>>>  test4(
        int id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltList<PageInfo<User>>>("test4",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltList,[FullType(PageInfo,[FullType(User)])])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：String
      /// 4:返回值在java中的类型为：List
      /// 5:返回值在java中的类型为：PageInfo
      /// 6:返回值在java中的类型为：User
          @GetMapping(value:"/test5",)
  Future<BuiltMap<String,BuiltList<PageInfo<User>>>>  test5(
        int id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,BuiltList<PageInfo<User>>>>("test5",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(PageInfo,[FullType(User)])])])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：Sex
      /// 4:返回值在java中的类型为：List
      /// 5:返回值在java中的类型为：PageInfo
      /// 6:返回值在java中的类型为：数组
      /// 7:返回值在java中的类型为：User
          @GetMapping(value:"/test6",)
  Future<BuiltMap<Sex,BuiltList<PageInfo<User>>>>  test6(
        int id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<Sex,BuiltList<PageInfo<User>>>>("test6",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(Sex),FullType(BuiltList,[FullType(PageInfo,[FullType(User)])])])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：Integer
      /// 4:返回值在java中的类型为：List
      /// 5:返回值在java中的类型为：PageInfo
      /// 6:返回值在java中的类型为：数组
      /// 7:返回值在java中的类型为：数组
      /// 8:返回值在java中的类型为：User
          @GetMapping(value:"/test7",)
  Future<BuiltMap<int,BuiltList<PageInfo<User>>>>  test7(
        int id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<int,BuiltList<PageInfo<User>>>>("test7",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(int),FullType(BuiltList,[FullType(PageInfo,[FullType(User)])])])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：Map
      /// 3:返回值在java中的类型为：String
      /// 4:返回值在java中的类型为：数组
      /// 5:返回值在java中的类型为：数组
      /// 6:返回值在java中的类型为：数组
      /// 7:返回值在java中的类型为：数组
      /// 8:返回值在java中的类型为：String
          @GetMapping(value:"/test8",)
  Future<BuiltMap<String,String>>  test8(
        int id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,String>>("test8",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(String)])
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：数组
      /// 3:返回值在java中的类型为：数组
      /// 4:返回值在java中的类型为：数组
      /// 5:返回值在java中的类型为：Map
      /// 6:返回值在java中的类型为：String
      /// 7:返回值在java中的类型为：数组
      /// 8:返回值在java中的类型为：String
          @GetMapping(value:"/test9",)
  Future<BuiltMap<String,String>>  test9(
        int id,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,String>>("test9",
  [id,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(String)])
          )
    
  );
  }
}


final userService = UserService();
