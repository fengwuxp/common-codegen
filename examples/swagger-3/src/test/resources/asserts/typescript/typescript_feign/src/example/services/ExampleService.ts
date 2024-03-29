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
FeignRequestOptions} from "feign-client";
      import {ExampleServiceGetMap3Req} from "../../req/ExampleServiceGetMap3Req";
      import {ExampleServiceGetMap2Req} from "../../req/ExampleServiceGetMap2Req";
      import {ExampleServiceGetMapsReq} from "../../req/ExampleServiceGetMapsReq";
      import {ExampleServiceGetMapReq} from "../../req/ExampleServiceGetMapReq";
      import {ExampleDto} from "../../ExampleDto";
      import {ExampleDTO} from "../../evt/ExampleDTO";
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
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：List
      * 3:返回值在java中的类型为：Integer
     **/
      @GetMapping({
            value:"get_num",
      })
    getNums!:(req: ExampleServiceGetNumsReq, option?: FeignRequestOptions) => Promise<Array<number>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：List
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Integer
      * 5:返回值在java中的类型为：String
     **/
      @GetMapping({
            value:"get_maps",
      })
    getMaps!:(req?: ExampleServiceGetMapsReq, option?: FeignRequestOptions) => Promise<Array<Record<number,string>>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：Integer
     **/
      @GetMapping({
            value:"get_map",
      })
    getMap!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Record<string,number>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Boolean
     **/
      @GetMapping({
            value:"get_map_2",
      })
    getMap2!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Record<string,Array<boolean>>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Boolean
     **/
      @GetMapping({
            value:"get_map_3/{id}",
      })
    getMap3!:(req: ExampleServiceGetMap3Req, option?: FeignRequestOptions) => Promise<Record<string,Array<boolean>>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Boolean
     **/
      @GetMapping({
            value:"get_map_/",
      })
    getMap4!:(req?: ExampleDTO, option?: FeignRequestOptions) => Promise<Record<string,Array<boolean>>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Boolean
     **/
      @GetMapping({
            value:"get_map_5",
      })
    getMap5!:(req?: ExampleDto, option?: FeignRequestOptions) => Promise<Record<string,Array<boolean>>>;
}

export default new ExampleService();
