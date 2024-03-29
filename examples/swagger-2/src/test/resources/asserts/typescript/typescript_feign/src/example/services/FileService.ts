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
      import {FileServiceDownloadReq} from "../../req/FileServiceDownloadReq";
      import {FileServiceDownload2Req} from "../../req/FileServiceDownload2Req";

    /**
     * 接口：GET
    **/
  @Feign({
        value:"/file",
  })
class FileService{

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：HttpEntity
      * 3:返回值在java中的类型为：InputStreamResource
     **/
      @GetMapping({
            value:"/download",
            consumes:[HttpMediaType.APPLICATION_STREAM],
      })
    download!:(req?: FileServiceDownloadReq, option?: FeignRequestOptions) => Promise<File>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：void
     **/
      @GetMapping({
            value:"/download_2",
            consumes:[HttpMediaType.APPLICATION_STREAM],
      })
    download2!:(req?: FileServiceDownload2Req, option?: FeignRequestOptions) => Promise<File>;
}

export default new FileService();
