package com.wuxp.codegen.core;

/**
 * 客户端的提供者类型
 *
 * @author wuxp
 */
public enum ClientProviderType {


    /**
     * https://github.com/OpenFeign/feign
     * openfeign client
     */
    OPENFEIGN,

    /**
     * https://github.com/spring-cloud/spring-cloud-openfeign
     * spring cloud openfeign client
     */
    SPRING_CLOUD_OPENFEIGN,

    /**
     * https://github.com/square/retrofit
     * okhttp retrofit
     */
    RETROFIT,

    /**
     * https://github.com/fengwuxp/fengwuxp_dart_feign
     */
    DART_FEIGN,

    /**
     * https://github.com/fengwuxp/fengwuxp-typescript-spring/tree/master/feign
     */
    TYPESCRIPT_FEIGN
}
