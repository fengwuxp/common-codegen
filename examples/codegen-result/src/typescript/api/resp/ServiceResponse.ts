
/**
 * 服务响应对象
**/

export interface  ServiceResponse<T> {

        /**
            *属性说明：响应数据code，示例输入：
            *在java中的类型为：Integer
        **/
        code?: number;
        /**
            *属性说明：响应数据，示例输入：
        **/
        data?: T;
        /**
            *属性说明：响应数据消息，示例输入：
            *在java中的类型为：String
        **/
        message?: string;
}