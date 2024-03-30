/* tslint:disable */
/* eslint-disable */

        import {Sort} from "./Sort";
import {DefaultOrderField} from "feign-client";



export interface  Pageable {

        paged: boolean;
        unpaged: boolean;
        offset: number;
        pageSize: number;
        pageNumber: number;
        sort: Sort;
}