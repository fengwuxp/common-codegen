/* tslint:disable */
  import request,{RequestOptionsInit} from 'umi-request';
      import {HelloServiceIndexReq} from "../../req/HelloServiceIndexReq";

    /**
     * 接口：GET
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:Http请求方法：GET
      * 2:Documented with OpenAPI v3 annotations
      * 3:标记忽略
      * 4:返回值在java中的类型为：String
     **/

export const  index=  ( options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/hello/hello`, {
      method: 'get',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

