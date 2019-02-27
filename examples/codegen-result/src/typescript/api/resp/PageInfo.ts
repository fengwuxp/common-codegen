
/**
 * 分页对象
**/

export interface  PageInfo<T> {

        /**
            *属性说明：查询页码，示例输入：
            *在java中的类型为：Integer
        **/
        queryPage?: number;
        /**
            *属性说明：查询大小，示例输入：
            *在java中的类型为：Integer
        **/
        querySize?: number;
        /**
            *属性说明：响应集合列表，示例输入：
            *在java中的类型为：List
        **/
        records?: Array<T>;
}