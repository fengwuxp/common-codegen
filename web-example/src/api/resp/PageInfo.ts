
/**
 **/

export interface  PageInfo<T> {

    /**
     *响应集合列表
     *在java中的类型为：List
     **/
    records?: Array<T>;
    /**
     *查询页码
     *在java中的类型为：Integer
     **/
    queryPage?: number;
    /**
     *查询大小
     *在java中的类型为：Integer
     **/
    querySize?: number;
}