/* tslint:disable */
import axios, {AxiosRequestConfig,AxiosResponse} from 'axios';
      import {HelloServiceIndexReq} from "../../req/HelloServiceIndexReq";

    /**
     * 接口：GET
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:GET /hello/hello
      * 2:Http请求方法：GET
      * 3:Documented with OpenAPI v3 annotations
      * 4:标记忽略
      * 5:返回值在java中的类型为：String
     **/

export const  index=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
  return axios.request<string>( {
      url:`/hello/hello`,
      method: 'get',
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

