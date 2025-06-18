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
        import {UserServiceGetUserReq} from "../../req/UserServiceGetUserReq";
        import {UserServiceSampleMapReq} from "../../req/UserServiceSampleMapReq";
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
        const API_FUNCTION_FACTORY = feignHttpFunctionBuilder({
            value:"/users",
        });
    /**
      * 1:GET /users
      * 2:获取用户列表
      * 3:Http请求方法：GET
      * 4:获取用户列表信息
      * @return 用户列表
      * 6:返回值在java中的类型为：List
      * 7:返回值在java中的类型为：User
     **/
    export const getUserList: FeignHttpClientPromiseFunction<void,Array<User>> = API_FUNCTION_FACTORY.get({
    });
    /**
      * 1:POST /users
      * 2:创建用户
      * 3:属性名称：user，属性说明：用户详细实体user，默认值：，示例输入：
      * 4:Http请求方法：POST
      * 5:根据前端的提交内容创建用户
      * @return 用户Id
      * 7:返回值在java中的类型为：Long
     **/
    export const postUser: FeignHttpClientPromiseFunction<UserServicePostUserReq ,string> = API_FUNCTION_FACTORY.post({
                queryArgNames:["user","order"],
    });
    /**
      * 1:GET /users/{id}
      * 2:获取用户详细信息
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：User
     **/
    export const getUser: FeignHttpClientPromiseFunction<UserServiceGetUserReq ,User> = API_FUNCTION_FACTORY.get({
                value:"/{id}",
    });
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
    export const putUser: FeignHttpClientPromiseFunction<UserServicePutUserReq ,string> = API_FUNCTION_FACTORY.put({
                value:"/{id}",
                queryArgNames:["user"],
    });
    /**
      * 1:DELETE /users/{id}
      * 2:删除用户
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：DELETE
      * 5:返回值在java中的类型为：String
     **/
    export const deleteUser: FeignHttpClientPromiseFunction<UserServiceDeleteUserReq ,string> = API_FUNCTION_FACTORY.delete({
                value:"/{id}",
    });
    /**
      * 1:GET /users/sample
      * 2:sample
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：String
     **/
    export const sample: FeignHttpClientPromiseFunction<UserServiceSampleReq |void,string> = API_FUNCTION_FACTORY.get({
    });
    /**
      * 1:GET /users/sample2
      * 2:sample
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：User
     **/
    export const sampleMap: FeignHttpClientPromiseFunction<UserServiceSampleMapReq |void,Record<string,User>> = API_FUNCTION_FACTORY.get({
                value:"sample2",
    });
    /**
      * 1:POST /users/uploadFile
      * 2:文件上传
      * 3:属性名称：file，属性说明：文件，默认值：，示例输入：
      * 4:Http请求方法：POST
      * 5:返回值在java中的类型为：void
     **/
    export const uploadFile: FeignHttpClientPromiseFunction<void,void> = API_FUNCTION_FACTORY.post({
                produces:[HttpMediaType.MULTIPART_FORM_DATA],
    });
    /**
      * 1:GET /users/test
      * 2:test3
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：Object
     **/
    export const test3: FeignHttpClientPromiseFunction<UserServiceTest3Req |void,Record<string,any>> = API_FUNCTION_FACTORY.get({
                value:"/test",
    });
