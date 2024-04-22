/* tslint:disable */
  import request,{RequestOptionsInit} from 'umi-request';
      import {Order} from "../../domain/Order";
      import {User} from "../../domain/User";

    /**
     * list tst
     * 接口：GET
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/

export const  test1=  (req: Array<User>, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/list`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/

export const  test2=  (req: User[], options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/list/test_2`, {
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/

export const  test3=  (req: Record<string,Order>, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/list/test_3`, {
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/

export const  test4=  (req: Array<User>, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/list/test_4`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/

export const  test5=  (req: Array<User>, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/list/test_5`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：String
     **/

export const  test6=  (req: Array<User>, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/list/test_6`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

