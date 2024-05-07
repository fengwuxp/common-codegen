package com.wuxp.codegen;

import com.wind.api.core.ApiResponse;
import com.wind.api.core.ImmutableApiResponse;
import com.wind.client.retrofit.query.AbstractPageQueryMap;
import com.wind.common.query.supports.AbstractPageQuery;
import com.wind.common.query.supports.DefaultOrderField;
import com.wind.common.query.supports.ImmutablePagination;
import com.wind.common.query.supports.Pagination;
import com.wind.common.query.supports.QueryOrderType;
import com.wind.common.query.supports.QueryType;

import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;

/**
 * Wind Api 模型常量定义
 *
 * @author wuxp
 * @date 2024-03-29 13:39
 **/
public final class WindApiModelConstants {

    private WindApiModelConstants() {
        throw new AssertionError();
    }

    private static final String TS_FEIGN_CLIENT_PACKAGE_PATH = "feign-client";

    private static final String PAGINATION_GENERIC_NAME = "ImmutablePagination<T>";

    private static final String API_RESPONSE_GENERIC_NAME = "ImmutableApiResponse<T>";

    public static final JavaCodeGenClassMeta JAVA_API_RESPONSE = new JavaCodeGenClassMeta(ImmutableApiResponse.class, API_RESPONSE_GENERIC_NAME, ImmutableApiResponse.class.getName(), true);

    public static final JavaCodeGenClassMeta JAVA_PAGINATION = new JavaCodeGenClassMeta(ImmutablePagination.class, PAGINATION_GENERIC_NAME, ImmutablePagination.class.getName(), true);

    public static final JavaCodeGenClassMeta JAVA_ABSTRACT_PAGE_QUERY = new JavaCodeGenClassMeta(AbstractPageQuery.class, null, AbstractPageQuery.class.getName(), true);

    public static final JavaCodeGenClassMeta JAVA_ABSTRACT_PAGE_QUERY_MAP = new JavaCodeGenClassMeta(AbstractPageQueryMap.class, null, AbstractPageQueryMap.class.getName(), true);

    public static final JavaCodeGenClassMeta JAVA_DEFAULT_ORDER_FIELD = new JavaCodeGenClassMeta(DefaultOrderField.class, null, DefaultOrderField.class.getName(), true);

    public static final JavaCodeGenClassMeta JAVA_QUERY_TYPE = new JavaCodeGenClassMeta(QueryType.class, null, QueryType.class.getName(), true);

    public static final JavaCodeGenClassMeta JAVA_QUERY_ORDER_TYPE = new JavaCodeGenClassMeta(QueryOrderType.class, null, QueryOrderType.class.getName(), true);


    public static final TypescriptClassMeta TS_API_RESPONSE = new TypescriptClassMeta(ApiResponse.class.getSimpleName(), "ApiResponse<T>", ClassType.INTERFACE, false, null, TS_FEIGN_CLIENT_PACKAGE_PATH);

    public static final TypescriptClassMeta TS_PAGINATION = new TypescriptClassMeta(Pagination.class.getSimpleName(), "Pagination<T>", ClassType.INTERFACE, false, null, TS_FEIGN_CLIENT_PACKAGE_PATH);

    public static final TypescriptClassMeta TS_ABSTRACT_PAGE_QUERY = new TypescriptClassMeta(AbstractPageQuery.class.getSimpleName(), null, ClassType.CLASS, true, null, TS_FEIGN_CLIENT_PACKAGE_PATH);

    public static final TypescriptClassMeta TS_DEFAULT_ORDER_FIELD = new TypescriptClassMeta(DefaultOrderField.class.getSimpleName(), null, ClassType.ENUM, false, null, TS_FEIGN_CLIENT_PACKAGE_PATH);

    public static final TypescriptClassMeta TS_QUERY_TYPE = new TypescriptClassMeta(QueryType.class.getSimpleName(), null, ClassType.ENUM, false, null, TS_FEIGN_CLIENT_PACKAGE_PATH);

    public static final TypescriptClassMeta TS_QUERY_ORDER_TYPE = new TypescriptClassMeta(QueryOrderType.class.getSimpleName(), null, ClassType.ENUM, false, null, TS_FEIGN_CLIENT_PACKAGE_PATH);
}
