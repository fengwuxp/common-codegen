/* tslint:disable */
  import request,{RequestOptionsInit} from 'umi-request';
      import {User} from "../../domain/User";
      import {Sex} from "../../enums/Sex";
      import {UserServiceSampleMapReq} from "../../req/UserServiceSampleMapReq";
      import {UserServiceGetUserReq} from "../../req/UserServiceGetUserReq";
      import {UserServiceDeleteUserReq} from "../../req/UserServiceDeleteUserReq";
      import {UserServiceSample2Req} from "../../req/UserServiceSample2Req";
      import {UserServiceTest8Req} from "../../req/UserServiceTest8Req";
      import {UserServiceTest6Req} from "../../req/UserServiceTest6Req";
      import {UserServiceTest4Req} from "../../req/UserServiceTest4Req";
      import {Order} from "../../domain/Order";
      import {UserServiceSampleReq} from "../../req/UserServiceSampleReq";
      import {UserServiceTest3Req} from "../../req/UserServiceTest3Req";
      import {PageInfo} from "../../resp/PageInfo";
      import {UserServicePutUserReq} from "../../req/UserServicePutUserReq";
      import {UserServiceTest7Req} from "../../req/UserServiceTest7Req";
      import {UserServiceTest9Req} from "../../req/UserServiceTest9Req";
      import {UserServiceTest5Req} from "../../req/UserServiceTest5Req";

    /**
     * 接口：GET
     * user
     * 通过这里配置使下面的映射都在/users下，可去除
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:GET /users/{id}
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：User
     **/

export const  getUser=  (req: UserServiceGetUserReq, options?: RequestOptionsInit): Promise<User> =>{
        const {id,...reqData} = req;
        const url =`/users/${id}`;
  return request<User>(url, {
      method: 'get',
      params: reqData,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:PUT /users/{id}
      * 2:Http请求方法：PUT
      * 3:返回值在java中的类型为：String
     **/

export const  putUser=  (req: UserServicePutUserReq, options?: RequestOptionsInit): Promise<string> =>{
        const {id,...reqData} = req;
        const url =`/users/${id}`;
  return request<string>(url, {
      method: 'put',
          requestType: 'json',
          data: reqData,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:DELETE /users/{id}
      * 2:Http请求方法：DELETE
      * 3:返回值在java中的类型为：String
     **/

export const  deleteUser=  (req: UserServiceDeleteUserReq, options?: RequestOptionsInit): Promise<string> =>{
        const {id,...reqData} = req;
        const url =`/users/${id}`;
  return request<string>(url, {
      method: 'delete',
      params: reqData,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/sample
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/

export const  sample=  (req?: UserServiceSampleReq, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/users/sample`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/sample3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：String
     **/

export const  sample2=  (req?: UserServiceSample2Req, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/users/sample3`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/sample2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：User
     **/

export const  sampleMap=  (req?: UserServiceSampleMapReq, options?: RequestOptionsInit): Promise<Record<string,User>> =>{
  return request<Record<string,User>>(`/users/sample2`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:POST /users/uploadFile
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：void
     **/

export const  uploadFile=  ( options?: RequestOptionsInit): Promise<void> =>{
  return request<void>(`/users/uploadFile`, {
      method: 'post',
          requestType: 'form',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/test
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Object
     **/

export const  test3=  (req?: UserServiceTest3Req, options?: RequestOptionsInit): Promise<Record<string,any>> =>{
  return request<Record<string,any>>(`/users/test`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/test2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：User
     **/

export const  test4=  (req?: UserServiceTest4Req, options?: RequestOptionsInit): Promise<Array<PageInfo<User>>> =>{
  return request<Array<PageInfo<User>>>(`/users/test2`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/test5
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：User
     **/

export const  test5=  (req?: UserServiceTest5Req, options?: RequestOptionsInit): Promise<Record<string,Array<PageInfo<User>>>> =>{
  return request<Record<string,Array<PageInfo<User>>>>(`/users/test5`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/test6
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：User
     **/

export const  test6=  (req?: UserServiceTest6Req, options?: RequestOptionsInit): Promise<Record<'MAN' | 'WOMAN' | 'NONE',Array<PageInfo<User>>>> =>{
  return request<Record<'MAN' | 'WOMAN' | 'NONE',Array<PageInfo<User>>>>(`/users/test6`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/test7
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Integer
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：PageInfo
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：User
     **/

export const  test7=  (req?: UserServiceTest7Req, options?: RequestOptionsInit): Promise<Record<number,Array<PageInfo<User>>>> =>{
  return request<Record<number,Array<PageInfo<User>>>>(`/users/test7`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/test8
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：String
     **/

export const  test8=  (req?: UserServiceTest8Req, options?: RequestOptionsInit): Promise<Record<string,string>> =>{
  return request<Record<string,string>>(`/users/test8`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /users/test9
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：Map
      * 7:返回值在java中的类型为：String
      * 8:返回值在java中的类型为：数组
      * 9:返回值在java中的类型为：String
     **/

export const  test9=  (req?: UserServiceTest9Req, options?: RequestOptionsInit): Promise<Record<string,string>> =>{
  return request<Record<string,string>>(`/users/test9`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

