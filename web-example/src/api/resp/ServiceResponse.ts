
/**
 **/

export interface  ServiceResponse<T> {

    /**
     *响应数据消息
     *在java中的类型为：String
     **/
    message?: string;
    /**
     *响应数据code
     *在java中的类型为：Integer
     **/
    code?: number;
    /**
     *响应数据
     **/
    data?: T;
}