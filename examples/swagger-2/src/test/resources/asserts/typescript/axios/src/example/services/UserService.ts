/* tslint:disable */
import axios, {AxiosRequestConfig,AxiosResponse} from 'axios';
      import {Order} from "../../domain/Order";
      import {User} from "../../domain/User";
      import {UserServiceSampleReq} from "../../req/UserServiceSampleReq";
      import {UserServiceGetUserReq} from "../../req/UserServiceGetUserReq";
      import {UserServiceSampleMapReq} from "../../req/UserServiceSampleMapReq";
      import {UserServicePostUserReq} from "../../req/UserServicePostUserReq";
      import {UserServiceDeleteUserReq} from "../../req/UserServiceDeleteUserReq";
      import {UserServiceGetUserListReq} from "../../req/UserServiceGetUserListReq";
      import {UserServiceTest3Req} from "../../req/UserServiceTest3Req";
      import {UserServicePutUserReq} from "../../req/UserServicePutUserReq";

    /**
     * 用户服务
     * 接口：GET
     * 用户服务（源码注释）
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:GET /users
      * 2:获取用户列表
      * 3:Http请求方法：GET
      * 4:获取用户列表信息
      * @return 用户列表
      * 6:返回值在java中的类型为：List
      * 7:返回值在java中的类型为：User
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
      * 1:POST /users
      * 2:创建用户
      * 3:属性名称：user，属性说明：用户详细实体user，默认值：，示例输入：
      * 4:Http请求方法：POST
      * 5:根据前端的提交内容创建用户
      * @return 用户Id
      * 7:返回值在java中的类型为：Long
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
      * 1:GET /users/{id}
      * 2:获取用户详细信息
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：User
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
      * 1:PUT /users/{id}
      * 2:更新用户详细信息
      * <pre>
      * 4:参数列表：
      * 5:参数名称：id，参数说明：null
      * 6:参数名称：user，参数说明：null
      * </pre>
      * 8:Http请求方法：PUT
      * 9:返回值在java中的类型为：String
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
      * 1:DELETE /users/{id}
      * 2:删除用户
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：DELETE
      * 5:返回值在java中的类型为：String
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
      * 1:GET /users/sample
      * 2:sample
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：String
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
      * 1:GET /users/sample2
      * 2:sample
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：User
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
      * 1:POST /users/uploadFile
      * 2:文件上传
      * 3:属性名称：file，属性说明：文件，默认值：，示例输入：
      * 4:Http请求方法：POST
      * 5:返回值在java中的类型为：void
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
      * 1:GET /users/test
      * 2:test3
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：Object
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

