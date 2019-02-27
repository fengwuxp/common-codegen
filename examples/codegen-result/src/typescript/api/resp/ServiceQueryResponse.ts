    import {ServiceResponse} from "./ServiceResponse";
    import {PageInfo} from "./PageInfo";

/**
 * 统一的查询响应对象
**/

export interface  ServiceQueryResponse<T> extends ServiceResponse<PageInfo<T>> {

}