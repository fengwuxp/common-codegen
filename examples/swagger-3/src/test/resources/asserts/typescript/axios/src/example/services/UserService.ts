/* tslint:disable */
import axios, {AxiosRequestConfig,AxiosResponse} from 'axios';
      import {User} from "../../domain/User";
      import {UserServiceSampleMapReq} from "../../req/UserServiceSampleMapReq";
      import {Sex} from "../../enums/Sex";
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
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：User
     **/

export const  getUser=  (req: UserServiceGetUserReq, options?: AxiosRequestConfig): Promise<AxiosResponse<User>> =>{
        const {id,...reqData} = req;
        const url =`/users/${id}`;
  return axios.request<User>( {
      url:url,
      method: 'get',
      params: reqData,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：PUT
      * 2:返回值在java中的类型为：String
     **/

export const  putUser=  (req: UserServicePutUserReq, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
        const {id,...reqData} = req;
        const url =`/users/${id}`;
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<string>( {
      url:url,
      method: 'put',
        headers,
          data: reqData,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：DELETE
      * 2:返回值在java中的类型为：String
     **/

export const  deleteUser=  (req: UserServiceDeleteUserReq, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
        const {id,...reqData} = req;
        const url =`/users/${id}`;
  return axios.request<string>( {
      url:url,
      method: 'delete',
      params: reqData,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：String
     **/

export const  sample=  (req?: UserServiceSampleReq, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
  return axios.request<string>( {
      url:`/users/sample`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：数组
      * 3:返回值在java中的类型为：String
     **/

export const  sample2=  (req?: UserServiceSample2Req, options?: AxiosRequestConfig): Promise<AxiosResponse<string[]>> =>{
  return axios.request<string[]>( {
      url:`/users/sample3`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：数组
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：User
     **/

export const  sampleMap=  (req?: UserServiceSampleMapReq, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,User[]>[]>> =>{
  return axios.request<Record<string,User[]>[]>( {
      url:`/users/sample2`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：void
     **/

export const  uploadFile=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<void>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'multipart/form-data';
  return axios.request<void>( {
      url:`/users/uploadFile`,
      method: 'post',
        headers,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：Object
     **/

export const  test3=  (req?: UserServiceTest3Req, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,any>>> =>{
  return axios.request<Record<string,any>>( {
      url:`/users/test`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：PageInfo
      * 5:返回值在java中的类型为：User
     **/

export const  test4=  (req?: UserServiceTest4Req, options?: AxiosRequestConfig): Promise<AxiosResponse<Array<PageInfo<User>>>> =>{
  return axios.request<Array<PageInfo<User>>>( {
      url:`/users/test2`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：User
     **/

export const  test5=  (req?: UserServiceTest5Req, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,Array<PageInfo<User>>>>> =>{
  return axios.request<Record<string,Array<PageInfo<User>>>>( {
      url:`/users/test5`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Sex
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：User
     **/

export const  test6=  (req?: UserServiceTest6Req, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<'MAN' | 'WOMAN' | 'NONE',Array<PageInfo<User[]>>>>> =>{
  return axios.request<Record<'MAN' | 'WOMAN' | 'NONE',Array<PageInfo<User[]>>>>( {
      url:`/users/test6`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Integer
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：PageInfo
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：User
     **/

export const  test7=  (req?: UserServiceTest7Req, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<number,Array<PageInfo<User[][]>>>>> =>{
  return axios.request<Record<number,Array<PageInfo<User[][]>>>>( {
      url:`/users/test7`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：数组
      * 6:返回值在java中的类型为：数组
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：String
     **/

export const  test8=  (req?: UserServiceTest8Req, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,string[][][][]>>> =>{
  return axios.request<Record<string,string[][][][]>>( {
      url:`/users/test8`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：数组
      * 3:返回值在java中的类型为：数组
      * 4:返回值在java中的类型为：数组
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：数组
      * 8:返回值在java中的类型为：String
     **/

export const  test9=  (req?: UserServiceTest9Req, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,string[]>[][][]>> =>{
  return axios.request<Record<string,string[]>[][][]>( {
      url:`/users/test9`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

