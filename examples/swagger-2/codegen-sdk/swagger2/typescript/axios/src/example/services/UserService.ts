/* tslint:disable */
import axios, {AxiosRequestConfig,AxiosResponse} from 'axios';
      import {Order} from "../../domain/Order";
      import {User} from "../../domain/User";
      import {UserServiceSampleReq} from "../../req/UserServiceSampleReq";
      import {UserServiceSampleMapReq} from "../../req/UserServiceSampleMapReq";
      import {UserServiceGetUserReq} from "../../req/UserServiceGetUserReq";
      import {Sex} from "../../enums/Sex";
      import {UserServicePostUserReq} from "../../req/UserServicePostUserReq";
      import {UserServiceDeleteUserReq} from "../../req/UserServiceDeleteUserReq";
      import {UserServiceGetUserListReq} from "../../req/UserServiceGetUserListReq";
      import {UserServiceTest3Req} from "../../req/UserServiceTest3Req";
      import {UserServicePutUserReq} from "../../req/UserServicePutUserReq";
      import {ExampleEnum} from "../../enums/ExampleEnum";

    /**
     * 用户服务
     * 接口：GET
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:获取用户列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：User
     **/

export const  getUserList=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<Array<User>>> =>{
  return axios.request<Array<User>>( {
      url:`/users`,
      method: 'get',
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:创建用户
      * 2:属性名称：user，属性说明：用户详细实体user，默认值：，示例输入：
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：Long
     **/

export const  postUser=  (req: UserServicePostUserReq, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<string>( {
      url:`/users`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:获取用户详细信息
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：User
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
      * 1:更新用户详细信息
      * <pre>
      * 3:参数列表：
      * 4:参数名称：id，参数说明：null
      * 5:参数名称：user，参数说明：null
      * </pre>
      * 7:Http请求方法：PUT
      * 8:返回值在java中的类型为：String
     **/

export const  putUser=  (req: UserServicePutUserReq, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
        const {id,...reqData} = req;
        const url =`/users${id==null?'':'/'+id}`;
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
      * 1:删除用户
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：DELETE
      * 4:返回值在java中的类型为：String
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
      * 1:sample
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：String
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
      * 1:sample
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：User
     **/

export const  sampleMap=  (req?: UserServiceSampleMapReq, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,User>>> =>{
  return axios.request<Record<string,User>>( {
      url:`/users/sample2`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:文件上传
      * 2:属性名称：file，属性说明：文件，默认值：，示例输入：
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：void
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
      * 1:test3
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：Object
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

