import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';

          import '../model/domain/order.dart';
          import '../model/domain/user.dart';


      /// 用户服务
      /// 接口：GET
      /// 用户服务（源码注释）
@Feign
  @FeignClient(value:"/users",)
class UserService extends FeignProxyClient {

UserService() : super() {

}


      /// 1:GET /users
      /// 2:获取用户列表
      /// 3:Http请求方法：GET
      /// 4:获取用户列表信息
      /// @return 用户列表
      /// 6:返回值在java中的类型为：List
      /// 7:返回值在java中的类型为：User
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

      /// 1:POST /users
      /// 2:创建用户
      /// 3:属性名称：user，属性说明：用户详细实体user，默认值：，示例输入：
      /// 4:Http请求方法：POST
      /// 5:根据前端的提交内容创建用户
      /// @return 用户Id
      /// 7:返回值在java中的类型为：Long
          @PostMapping()
  Future<int>  postUser(
          @RequestBody()
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

      /// 1:GET /users/{id}
      /// 2:获取用户详细信息
      /// 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 4:Http请求方法：GET
      /// 5:返回值在java中的类型为：User
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

      /// 1:PUT /users/{id}
      /// 2:更新用户详细信息
      /// <pre>
      /// 4:参数列表：
      /// 5:参数名称：id，参数说明：null
      /// 6:参数名称：user，参数说明：null
      /// </pre>
      /// 8:Http请求方法：PUT
      /// 9:返回值在java中的类型为：String
          @PutMapping(value:"/{id}",)
  Future<String>  putUser(
          @PathVariable(name: "id" ,required: false )
        int id,
          @RequestBody()
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

      /// 1:DELETE /users/{id}
      /// 2:删除用户
      /// 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 4:Http请求方法：DELETE
      /// 5:返回值在java中的类型为：String
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

      /// 1:GET /users/sample
      /// 2:sample
      /// 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 4:Http请求方法：GET
      /// 5:返回值在java中的类型为：String
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

      /// 1:GET /users/sample2
      /// 2:sample
      /// 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 4:Http请求方法：GET
      /// 5:返回值在java中的类型为：Map
      /// 6:返回值在java中的类型为：String
      /// 7:返回值在java中的类型为：User
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

      /// 1:POST /users/uploadFile
      /// 2:文件上传
      /// 3:属性名称：file，属性说明：文件，默认值：，示例输入：
      /// 4:Http请求方法：POST
      /// 5:返回值在java中的类型为：void
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

      /// 1:GET /users/test
      /// 2:test3
      /// 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      /// 4:Http请求方法：GET
      /// 5:返回值在java中的类型为：Map
      /// 6:返回值在java中的类型为：String
      /// 7:返回值在java中的类型为：Object
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
