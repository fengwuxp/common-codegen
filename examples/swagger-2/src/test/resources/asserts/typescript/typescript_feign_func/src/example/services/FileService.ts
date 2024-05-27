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
        const API_FUNCTION_FACTORY = feignHttpFunctionBuilder({
            value:"/file",
        });
    /**
      * 1:GET /file/download
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：HttpEntity
      * 4:返回值在java中的类型为：InputStreamResource
     **/
    export const download: FeignHttpClientPromiseFunction<FileServiceDownloadReq |void,File> = API_FUNCTION_FACTORY.get({
                value:"/download",
                consumes:[HttpMediaType.APPLICATION_STREAM],
    });
    /**
      * 1:GET /file/download_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/
    export const download2: FeignHttpClientPromiseFunction<FileServiceDownload2Req |void,File> = API_FUNCTION_FACTORY.get({
                value:"/download_2",
                consumes:[HttpMediaType.APPLICATION_STREAM],
    });
