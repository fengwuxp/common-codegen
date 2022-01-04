/* tslint:disable */
/* eslint-disable */

import {
Feign,
RequestMapping,
PostMapping,
DeleteMapping,
GetMapping,
PutMapping,
Signature,
HttpMediaType,
AuthenticationType,
FeignRequestOptions} from "fengwuxp-typescript-feign";
      import {User} from "../../domain/User";
      import {UserServiceSampleMapReq} from "../../req/UserServiceSampleMapReq";
      import {Sex} from "../../enums/Sex";
      import {UserServiceGetUserReq} from "../../req/UserServiceGetUserReq";
      import {UserServiceDeleteUserReq} from "../../req/UserServiceDeleteUserReq";
      import {UserServiceSample2Req} from "../../req/UserServiceSample2Req";
      import {UserServiceTest8Req} from "../../req/UserServiceTest8Req";
      import {UserServiceTest6Req} from "../../req/UserServiceTest6Req";
      import {UserServiceTest4Req} from "../../req/UserServiceTest4Req";
      import {Order} from "../../domain/Order";
      import {UserServiceSampleReq} from "../../req/UserServiceSampleReq";
      import {UserServiceTest3Req} from "../../req/UserServiceTest3Req";
      import {PageInfo} from "../../resp/PageInfo";
      import {UserServicePutUserReq} from "../../req/UserServicePutUserReq";
      import {UserServiceTest7Req} from "../../req/UserServiceTest7Req";
      import {UserServiceTest9Req} from "../../req/UserServiceTest9Req";
      import {UserServiceTest5Req} from "../../req/UserServiceTest5Req";

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
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/{id}",
      })
    getUser!:(req?: UserServiceGetUserReq, option?: FeignRequestOptions) => Promise<User>;
    /**
      * 1:Http请求方法：PUT
      * 2:返回值在java中的类型为：String
     **/
      @PutMapping({
            value:"/{id}",
      })
    putUser!:(req?: UserServicePutUserReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:Http请求方法：DELETE
      * 2:返回值在java中的类型为：String
     **/
      @DeleteMapping({
            value:"/{id}",
      })
    deleteUser!:(req?: UserServiceDeleteUserReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GetMapping({
      })
    sample!:(req?: UserServiceSampleReq, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：数组
      * 3:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"sample3",
      })
    sample2!:(req?: UserServiceSample2Req, option?: FeignRequestOptions) => Promise<string[]>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：数组
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"sample2",
      })
    sampleMap!:(req?: UserServiceSampleMapReq, option?: FeignRequestOptions) => Promise<Record<string,User[]>[]>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：void
     **/
      @PostMapping({
            produces:[HttpMediaType.MULTIPART_FORM_DATA],
      })
    uploadFile!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<void>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：Object
     **/
      @GetMapping({
            value:"/test",
      })
    test3!:(req?: UserServiceTest3Req, option?: FeignRequestOptions) => Promise<Record<string,any>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：PageInfo
      * 5:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/test2",
      })
    test4!:(req?: UserServiceTest4Req, option?: FeignRequestOptions) => Promise<Array<PageInfo<User>>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/test5",
      })
    test5!:(req?: UserServiceTest5Req, option?: FeignRequestOptions) => Promise<Record<string,Array<PageInfo<User>>>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Sex
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/test6",
      })
    test6!:(req?: UserServiceTest6Req, option?: FeignRequestOptions) => Promise<Record<'MAN' | 'WOMAN' | 'NONE',Array<PageInfo<User[]>>>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Integer
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：User
     **/
      @GetMapping({
            value:"/test7",
      })
    test7!:(req?: UserServiceTest7Req, option?: FeignRequestOptions) => Promise<Record<number,Array<PageInfo<User[][]>>>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"/test8",
      })
    test8!:(req?: UserServiceTest8Req, option?: FeignRequestOptions) => Promise<Record<string,string[][][][]>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：数组
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"/test9",
      })
    test9!:(req?: UserServiceTest9Req, option?: FeignRequestOptions) => Promise<Record<string,string[]>[][][]>;
}

export default new UserService();
