/* tslint:disable */
/* eslint-disable */

        import {Sort} from "./Sort";



export interface  Pageable {

        paged: boolean;
        unpaged: boolean;
        offset: number;
        pageSize: number;
        pageNumber: number;
        sort: Sort;
}