/* tslint:disable */
/* eslint-disable */

import {
Feign,
RequestMapping,
PostMapping,
DeleteMapping,
GetMapping,
PutMapping,
FeignHttpClientPromiseFunction,
feignHttpFunctionBuilder,
FeignRequestOptions} from "feign-client";
import {HttpMediaType} from "wind-common-utils/lib/http/HttpMediaType";
      import {Order} from "../../domain/Order";
      import {User} from "../../domain/User";
      import {UserServiceSampleReq} from "../../req/UserServiceSampleReq";
      import {UserServiceSampleMapReq} from "../../req/UserServiceSampleMapReq";
      import {UserServiceGetUserReq} from "../../req/UserServiceGetUserReq";
      import {UserServicePostUserReq} from "../../req/UserServicePostUserReq";
      import {UserServiceDeleteUserReq} from "../../req/UserServiceDeleteUserReq";
      import {UserServiceGetUserListReq} from "../../req/UserServiceGetUserListReq";
      import {UserServiceTest3Req} from "../../req/UserServiceTest3Req";
      import {UserServicePutUserReq} from "../../req/UserServicePutUserReq";

    /**
     * 用户服务
     * 接口：GET
     * 用户服务（源码注释）
    **/
  @Feign({
        value:"/users",
  })
class UserService{

    /**
      * 1:GET /users
      * 2:获取用户列表
      * 3:Http请求方法：GET
      * 4:获取用户列表信息
      * @return 用户列表
      * 6:返回值在java中的类型为：List
      * 7:返回值在java中的类型为：User
     **/
      @GetMapping({
      })
    getUserList!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Array<User>>;
    /**
      * 1:POST /users
      * 2:创建用户
      * 3:属性名称：user，属性说明：用户详细实体user，默认值：，示例输入：
      * 4:Http请求方法：POST
      * 5:根据前端的提交内容创建用户
      * @return 用户Id
      * 7:返回值在java中的类型为：Long
     **/
      @PostMapping({
            queryArgNames:["user","order"],
      })
    postUser!:(req: UserServicePostUserReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:GET /users/{id}
      * 2:获取用户详细信息
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/{id}",
      })
    getUser!:(req: UserServiceGetUserReq, option?: FeignRequestOptions) => Promise<User>;
    /**
      * 1:PUT /users/{id}
      * 2:更新用户详细信息
      * <pre>
      * 4:参数列表：
      * 5:参数名称：id，参数说明：null
      * 6:参数名称：user，参数说明：null
      * </pre>
      * 8:Http请求方法：PUT
      * 9:返回值在java中的类型为：String
     **/
      @PutMapping({
            value:"/{id}",
            queryArgNames:["user"],
      })
    putUser!:(req: UserServicePutUserReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:DELETE /users/{id}
      * 2:删除用户
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：DELETE
      * 5:返回值在java中的类型为：String
     **/
      @DeleteMapping({
            value:"/{id}",
      })
    deleteUser!:(req: UserServiceDeleteUserReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:GET /users/sample
      * 2:sample
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：String
     **/
      @GetMapping({
      })
    sample!:(req?: UserServiceSampleReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:GET /users/sample2
      * 2:sample
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"sample2",
      })
    sampleMap!:(req?: UserServiceSampleMapReq, option?: FeignRequestOptions) => Promise<Record<string,User>>;
    /**
      * 1:POST /users/uploadFile
      * 2:文件上传
      * 3:属性名称：file，属性说明：文件，默认值：，示例输入：
      * 4:Http请求方法：POST
      * 5:返回值在java中的类型为：void
     **/
      @PostMapping({
            produces:[HttpMediaType.MULTIPART_FORM_DATA],
      })
    uploadFile!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<void>;
    /**
      * 1:GET /users/test
      * 2:test3
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：Object
     **/
      @GetMapping({
            value:"/test",
      })
    test3!:(req?: UserServiceTest3Req, option?: FeignRequestOptions) => Promise<Record<string,any>>;
}

export default new UserService();
