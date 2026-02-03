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
      import {ExampleServiceGetMap3Req} from "../../req/ExampleServiceGetMap3Req";
      import {ExampleServiceGetMap2Req} from "../../req/ExampleServiceGetMap2Req";
      import {User} from "../../domain/User";
      import {BaseInfo} from "../../domain/BaseInfo";
      import {ExampleServiceGetMapsReq} from "../../req/ExampleServiceGetMapsReq";
      import {ExampleServiceGetMapReq} from "../../req/ExampleServiceGetMapReq";
      import {ExampleDTO} from "../../resp/ExampleDTO";
      import {ExampleServiceGetNumsReq} from "../../req/ExampleServiceGetNumsReq";

    /**
     * 接口：GET
     * example_cms
    **/
  @Feign({
        value:"/example_cms",
  })
class ExampleService{

    /**
      * 1:GET /example_cms/get_num
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Integer
     **/
      @GetMapping({
            value:"get_num",
      })
    getNums!:(req: ExampleServiceGetNumsReq, option?: FeignRequestOptions) => Promise<Array<number>>;
    /**
      * 1:GET /example_cms/get_maps
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：Integer
      * 6:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"get_maps",
      })
    getMaps!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Array<Record<number,string>>>;
    /**
      * 1:GET /example_cms/get_map
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Integer
     **/
      @GetMapping({
            value:"get_map",
      })
    getMap!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Record<string,number>>;
    /**
      * 1:GET /example_cms/get_map_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GetMapping({
            value:"get_map_2",
      })
    getMap2!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Record<string,Array<boolean>>>;
    /**
      * 1:GET /example_cms/get_map_3/{id}
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GetMapping({
            value:"get_map_3/{id}",
      })
    getMap3!:(req: ExampleServiceGetMap3Req, option?: FeignRequestOptions) => Promise<Record<string,Array<boolean>>>;
    /**
      * 1:GET /example_cms/get_map_/
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GetMapping({
            value:"get_map_/",
      })
    getMap4!:(req?: ExampleDTO, option?: FeignRequestOptions) => Promise<Record<string,Array<boolean>>>;
    /**
      * 1:GET /example_cms/get_map_5
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/
      @GetMapping({
            value:"get_map_5",
      })
    getMap5!:(req?: ExampleDTO, option?: FeignRequestOptions) => Promise<Record<string,Array<boolean>>>;
    /**
      * 1:GET /example_cms/get_map_6
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GetMapping({
            value:"get_map_6",
      })
    example0!:(req?: BaseInfo<string,User>, option?: FeignRequestOptions) => Promise<void>;
    /**
      * 1:GET /example_cms/get_map_7
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GetMapping({
            value:"get_map_7",
      })
    example2!:(req: Record<string,User>, option?: FeignRequestOptions) => Promise<void>;
    /**
      * 1:GET /example_cms/get_map_8
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
      @GetMapping({
            value:"get_map_8",
      })
    example3!:(req: Array<User>, option?: FeignRequestOptions) => Promise<void>;
}

export default new ExampleService();
