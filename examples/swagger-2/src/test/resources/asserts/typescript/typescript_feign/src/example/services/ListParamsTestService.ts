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
      import {Order} from "../../domain/Order";
      import {User} from "../../domain/User";

    /**
     * list tst
     * 接口：GET
     * list test（源码注释）
    **/
  @Feign({
        value:"/list",
  })
class ListParamsTestService{

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @PostMapping({
      })
    test1!:(req: Array<User>, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"test_2",
      })
    test2!:(req: User[], option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"test_3",
      })
    test3!:(req: Record<string,Order>, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @PostMapping({
            value:"test_4",
      })
    test4!:(req: Array<User>, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @PostMapping({
            value:"test_5",
      })
    test5!:(req: Array<User>, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/
      @PostMapping({
            value:"test_6",
      })
    test6!:(req: Array<User>, option?: FeignRequestOptions) => Promise<string>;
}

export default new ListParamsTestService();
