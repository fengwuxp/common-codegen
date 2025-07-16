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
      import {User} from "../../domain/User";
      import {Sex} from "../../enums/Sex";
      import {Order} from "../../domain/Order";
      import {PageInfo} from "../../resp/PageInfo";

    /**
     * 接口：GET
     * user
     * 通过这里配置使下面的映射都在/users下，可去除
    **/
  @Feign({
        value:"/users",
  })
class UserService{

    /**
      * 1:GET /users/{id}
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/{id}",
      })
    getUser!:(req: UserServiceGetUserReq, option?: FeignRequestOptions) => Promise<User>;
    /**
      * 1:PUT /users/{id}
      * 2:Http请求方法：PUT
      * 3:返回值在java中的类型为：String
     **/
      @PutMapping({
            value:"/{id}",
            queryArgNames:["user","order"],
      })
    putUser!:(req: UserServicePutUserReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:DELETE /users/{id}
      * 2:Http请求方法：DELETE
      * 3:返回值在java中的类型为：String
     **/
      @DeleteMapping({
            value:"/{id}",
      })
    deleteUser!:(req: UserServiceDeleteUserReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:GET /users/sample
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/
      @GetMapping({
      })
    sample!:(req?: UserServiceSampleReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:GET /users/sample3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"sample3",
      })
    sample2!:(req?: UserServiceSample2Req, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:GET /users/sample2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"sample2",
            queryArgNames:["sex"],
      })
    sampleMap!:(req?: UserServiceSampleMapReq, option?: FeignRequestOptions) => Promise<Record<string,User>>;
    /**
      * 1:POST /users/uploadFile
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：void
     **/
      @PostMapping({
            produces:[HttpMediaType.MULTIPART_FORM_DATA],
      })
    uploadFile!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<void>;
    /**
      * 1:GET /users/test
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Object
     **/
      @GetMapping({
            value:"/test",
      })
    test3!:(req?: UserServiceTest3Req, option?: FeignRequestOptions) => Promise<Record<string,any>>;
    /**
      * 1:GET /users/test2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/test2",
      })
    test4!:(req?: UserServiceTest4Req, option?: FeignRequestOptions) => Promise<Array<PageInfo<User>>>;
    /**
      * 1:GET /users/test5
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/test5",
      })
    test5!:(req?: UserServiceTest5Req, option?: FeignRequestOptions) => Promise<Record<string,Array<PageInfo<User>>>>;
    /**
      * 1:GET /users/test6
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/test6",
      })
    test6!:(req?: UserServiceTest6Req, option?: FeignRequestOptions) => Promise<Record<'MAN' | 'WOMAN' | 'NONE',Array<PageInfo<User>>>>;
    /**
      * 1:GET /users/test7
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Integer
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/test7",
      })
    test7!:(req?: UserServiceTest7Req, option?: FeignRequestOptions) => Promise<Record<number,Array<PageInfo<User>>>>;
    /**
      * 1:GET /users/test8
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"/test8",
      })
    test8!:(req?: UserServiceTest8Req, option?: FeignRequestOptions) => Promise<Record<string,string>>;
    /**
      * 1:GET /users/test9
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：Map
      * 7:返回值在java中的类型为：String
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"/test9",
      })
    test9!:(req?: UserServiceTest9Req, option?: FeignRequestOptions) => Promise<Record<string,string>>;
}

export default new UserService();
