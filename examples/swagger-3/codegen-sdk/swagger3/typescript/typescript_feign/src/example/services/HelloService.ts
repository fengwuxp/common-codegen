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
      import {HelloServiceIndexReq} from "../../req/HelloServiceIndexReq";

    /**
     * 接口：GET
    **/
  @Feign({
        value:"/hello",
  })
class HelloService{

    /**
      * 1:Http请求方法：GET
      * 2:Documented with OpenAPI v3 annotations
      * 3:标记忽略
      * 4:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"/hello",
      })
    index!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<string>;
}

export default new HelloService();
