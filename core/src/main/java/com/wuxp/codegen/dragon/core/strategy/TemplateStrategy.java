package com.wuxp.codegen.dragon.core.strategy;

/**
 * 模板处理策略
 */
public interface TemplateStrategy<T> {

    /**
     * api 接口的模板名称
     */
    String API_SERVICE_TEMPLATE_NAME = "api_service.ftl";

    /**
     * api 接口请求对象的模板名称
     */
    String API_REQUEST_TEMPLATE_NAME = "api_request.ftl";

    /**
     * api 接口响应对象的模板名称
     */
    String API_RESPONSE_TEMPLATE_NAME = "api_response.ftl";

    /**
     * 枚举对象的模板名称
     */
    String API_ENUM_TEMPLATE_NAME = "api_enum.ftl";

    /**
     * 使用模板构建目标代码
     *
     * @param data
     */
    void build(T data);

}
