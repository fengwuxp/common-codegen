import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';

          import '../model/domain/order.dart';
          import '../model/domain/user.dart';
          import '../serializers.dart';


      /// 用户服务
      /// 接口：GET
      /// 用户服务（源码注释）
@Feign
  @FeignClient(value:"/users",)
class UserService extends FeignProxyClient {

UserService() : super() {

}


      /// 1:获取用户列表
      /// 2:Http请求方法：GET
      /// 3:获取用户列表信息
      /// @return 用户列表
      /// 5:返回值在java中的类型为：List
      /// 6:返回值在java中的类型为：User
          @GetMapping()
  Future<BuiltList<User>>  getUserList(
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltList<User>>("getUserList",
  [],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltList,[FullType(User)])
          )
    
  );
  }

      /// 1:创建用户
      /// 2:属性名称：user，属性说明：用户详细实体user，默认值：，示例输入：
      /// 3:Http请求方法：POST
      /// 4:根据前端的提交内容创建用户
      /// @return 用户Id
      /// 6:返回值在java中的类型为：Long
          @PostMapping()
  Future<int>  postUser(
          @RequestBody(required: true )
        User user,
        Order order,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<int>("postUser",
  [user,order,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(int)
          )
    
  );
  }

      /// 1:获取用户详细信息
      /// 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 3:Http请求方法：GET
      /// 4:返回值在java中的类型为：User
          @GetMapping(value:"/{id}",)
  Future<User>  getUser(
          @PathVariable(name: "id" ,required: true )
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

      /// 1:更新用户详细信息
      /// <pre>
      /// 3:参数列表：
      /// 4:参数名称：id，参数说明：null
      /// 5:参数名称：user，参数说明：null
      /// </pre>
      /// 7:Http请求方法：PUT
      /// 8:返回值在java中的类型为：String
          @PutMapping(value:"/{id}",)
  Future<String>  putUser(
          @PathVariable(name: "id" ,required: false )
        int id,
          @RequestBody(required: true )
        User user,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<String>("putUser",
  [id,user,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(String)
          )
    
  );
  }

      /// 1:删除用户
      /// 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 3:Http请求方法：DELETE
      /// 4:返回值在java中的类型为：String
          @DeleteMapping(value:"/{id}",)
  Future<String>  deleteUser(
          @PathVariable(name: "id" ,required: true )
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

      /// 1:sample
      /// 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 3:Http请求方法：GET
      /// 4:返回值在java中的类型为：String
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

      /// 1:sample
      /// 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 3:Http请求方法：GET
      /// 4:返回值在java中的类型为：Map
      /// 5:返回值在java中的类型为：String
      /// 6:返回值在java中的类型为：User
          @GetMapping(value:"sample2",)
  Future<BuiltMap<String,User>>  sampleMap(
        BuiltList<int> ids,
        String name,
        BuiltList<BuiltMap<String,BuiltList<String>>> testParam,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<BuiltMap<String,User>>("sampleMap",
  [ids,name,testParam,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
              specifiedType:FullType(BuiltMap,[FullType(String),FullType(User)])
          )
    
  );
  }

      /// 1:文件上传
      /// 2:属性名称：file，属性说明：文件，默认值：，示例输入：
      /// 3:Http请求方法：POST
      /// 4:返回值在java中的类型为：void
          @PostMapping(produces:[HttpMediaType.MULTIPART_FORM_DATA],)
  Future<void>  uploadFile(
          @RequestParam(name: "file" ,required: true )
        File commonsMultipartFile,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<void>("uploadFile",
  [commonsMultipartFile,],
    feignOptions: feignOptions
  );
  }

      /// 1:test3
      /// 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 3:Http请求方法：GET
      /// 4:返回值在java中的类型为：Map
      /// 5:返回值在java中的类型为：String
      /// 6:返回值在java中的类型为：Object
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
}


final userService = UserService();
