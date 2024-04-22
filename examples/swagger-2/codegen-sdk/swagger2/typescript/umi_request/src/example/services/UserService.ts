/* tslint:disable */
  import request,{RequestOptionsInit} from 'umi-request';
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

export const  getUserList=  ( options?: RequestOptionsInit): Promise<Array<User>> =>{
  return request<Array<User>>(`/users`, {
      method: 'get',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:创建用户
      * 2:属性名称：user，属性说明：用户详细实体user，默认值：，示例输入：
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：Long
     **/

export const  postUser=  (req: UserServicePostUserReq, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/users`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:获取用户详细信息
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：User
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
      * 1:更新用户详细信息
      * <pre>
      * 3:参数列表：
      * 4:参数名称：id，参数说明：null
      * 5:参数名称：user，参数说明：null
      * </pre>
      * 7:Http请求方法：PUT
      * 8:返回值在java中的类型为：String
     **/

export const  putUser=  (req: UserServicePutUserReq, options?: RequestOptionsInit): Promise<string> =>{
        const {id,...reqData} = req;
        const url =`/users${id==null?'':'/'+id}`;
  return request<string>(url, {
      method: 'put',
          requestType: 'json',
          data: reqData,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:删除用户
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：DELETE
      * 4:返回值在java中的类型为：String
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
      * 1:sample
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：String
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
      * 1:sample
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：User
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
      * 1:文件上传
      * 2:属性名称：file，属性说明：文件，默认值：，示例输入：
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：void
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
      * 1:test3
      * 2:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：String
      * 6:返回值在java中的类型为：Object
     **/

export const  test3=  (req?: UserServiceTest3Req, options?: RequestOptionsInit): Promise<Record<string,any>> =>{
  return request<Record<string,any>>(`/users/test`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

