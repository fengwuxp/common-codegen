/* tslint:disable */
  import request,{RequestOptionsInit} from 'umi-request';
      import {ExampleDTO} from "../../resp/ExampleDTO";

    /**
     * 接口：GET
     * example_cms
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:GET /example_cms/get_num
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Integer
     **/

export const  getNums=  (req: ExampleServiceGetNumsReq, options?: RequestOptionsInit): Promise<Array<number>> =>{
  return request<Array<number>>(`/example_cms/get_num`, {
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /example_cms/get_maps
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：Integer
      * 6:返回值在java中的类型为：String
     **/

export const  getMaps=  ( options?: RequestOptionsInit): Promise<Array<Record<number,string>>> =>{
  return request<Array<Record<number,string>>>(`/example_cms/get_maps`, {
      method: 'get',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /example_cms/get_map
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Integer
     **/

export const  getMap=  ( options?: RequestOptionsInit): Promise<Record<string,number>> =>{
  return request<Record<string,number>>(`/example_cms/get_map`, {
      method: 'get',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /example_cms/get_map_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/

export const  getMap2=  ( options?: RequestOptionsInit): Promise<Record<string,Array<boolean>>> =>{
  return request<Record<string,Array<boolean>>>(`/example_cms/get_map_2`, {
      method: 'get',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /example_cms/get_map_3/{id}
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/

export const  getMap3=  (req: ExampleServiceGetMap3Req, options?: RequestOptionsInit): Promise<Record<string,Array<boolean>>> =>{
        const {id,...reqData} = req;
        const url =`/example_cms/get_map_3/${id}`;
  return request<Record<string,Array<boolean>>>(url, {
      method: 'get',
      params: reqData,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /example_cms/get_map_/
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/

export const  getMap4=  (req?: ExampleDTO, options?: RequestOptionsInit): Promise<Record<string,Array<boolean>>> =>{
  return request<Record<string,Array<boolean>>>(`/example_cms/get_map_/`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:GET /example_cms/get_map_5
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/

export const  getMap5=  (req?: ExampleDTO, options?: RequestOptionsInit): Promise<Record<string,Array<boolean>>> =>{
  return request<Record<string,Array<boolean>>>(`/example_cms/get_map_5`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

