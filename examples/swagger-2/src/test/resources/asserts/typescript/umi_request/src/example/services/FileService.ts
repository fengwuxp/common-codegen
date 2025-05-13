/* tslint:disable */
  import request,{RequestOptionsInit} from 'umi-request';
      import {FileServiceDownloadReq} from "../../req/FileServiceDownloadReq";
      import {FileServiceDownload2Req} from "../../req/FileServiceDownload2Req";

    /**
     * 接口：GET
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:GET /file/download
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：HttpEntity
      * 4:返回值在java中的类型为：InputStreamResource
     **/

export const  download=  (req?: FileServiceDownloadReq, options?: RequestOptionsInit): Promise<File> =>{
  return request<File>(`/file/download`, {
      method: 'get',
      params: req || {},
      responseType: 'blob',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /file/download_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/

export const  download2=  (req?: FileServiceDownload2Req, options?: RequestOptionsInit): Promise<File> =>{
  return request<File>(`/file/download_2`, {
      method: 'get',
      params: req || {},
      responseType: 'blob',
  ...(options || {} as RequestOptionsInit)
  })
  }

