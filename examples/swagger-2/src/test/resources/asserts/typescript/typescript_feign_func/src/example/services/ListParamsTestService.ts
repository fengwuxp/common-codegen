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

    /**
     * list tst
     * 接口：GET
     * list test（源码注释）
    **/
        const API_FUNCTION_FACTORY = feignHttpFunctionBuilder({
            value:"/list",
        });
    /**
      * 1:POST /list
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/
    export const test1: FeignHttpClientPromiseFunction<Array<User> ,string> = API_FUNCTION_FACTORY.post({
    });
    /**
      * 1:GET /list/test_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/
    export const test2: FeignHttpClientPromiseFunction<User[] ,string> = API_FUNCTION_FACTORY.get({
                value:"test_2",
    });
    /**
      * 1:GET /list/test_3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/
    export const test3: FeignHttpClientPromiseFunction<Record<string,Order> ,string> = API_FUNCTION_FACTORY.get({
                value:"test_3",
    });
    /**
      * 1:POST /list/test_4
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/
    export const test4: FeignHttpClientPromiseFunction<Array<User> ,string> = API_FUNCTION_FACTORY.post({
                value:"test_4",
    });
    /**
      * 1:POST /list/test_5
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/
    export const test5: FeignHttpClientPromiseFunction<Array<User> ,string> = API_FUNCTION_FACTORY.post({
                value:"test_5",
    });
    /**
      * 1:POST /list/test_6
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/
    export const test6: FeignHttpClientPromiseFunction<Array<User> ,string> = API_FUNCTION_FACTORY.post({
                value:"test_6",
    });
