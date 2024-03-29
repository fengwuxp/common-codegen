import {RouteProps} from "react-router";
import * as React from "react";
import {RouteConfig} from "react-router-config";

export interface AppRouterAuthenticator<T> {

    /**
     * 是否已经鉴权
     */
    isAuthenticated: () => Promise<void>;

    /**
     * 获取登录页面
     */
    authenticationView?: () => string;
}

/**
 * private route props
 */
export interface PrivateRouteProps extends RouteProps {
    /**
     * 路由鉴权者
     */
    routeAuthenticator?: AppRouterAuthenticator<any>;

    extraProps?: {
        [propName: string]: any
    };
}


/**
 * 私有路由的定义
 */
export type PrivateRoute<T = any> = React.ComponentType<T>;


export interface AuthenticatedRouteConfig extends RouteConfig {

    /**
     * 路由是否需要鉴权，默认：false
     */
    requiredAuthentication?: boolean;

    /**
     * 路由名称
     */
    name?: string;

    routes?: AuthenticatedRouteConfig[];
}
