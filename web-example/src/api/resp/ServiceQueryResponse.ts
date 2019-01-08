    import {ServiceResponse} from "@/src/api/resp/ServiceResponse";
    import {PageInfo} from "@/src/api/resp/PageInfo";

/**
 * 统一的查询响应对象
**/

export interface  ServiceQueryResponse<T> extends ServiceResponse<PageInfo<T>> {

}