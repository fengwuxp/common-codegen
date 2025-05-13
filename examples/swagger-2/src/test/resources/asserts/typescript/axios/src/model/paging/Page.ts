/* tslint:disable */
/* eslint-disable */

        import {Pageable} from "./Pageable";
        import {Sort} from "./Sort";
import {DefaultOrderField} from "feign-client";



export interface  Page<T> {

        content: Array<T>;
        empty: boolean;
        first: boolean;
        last: boolean;
        number: number;
        numberOfElements: number;
        size: number;
        totalElements: number;
        totalPages: number;
        pageable: Pageable;
        sort: Sort;
}