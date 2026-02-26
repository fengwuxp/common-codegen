package com.wuxp.codegen.meta.annotations.factories.spring.service;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @author wuxp
 * @date 2026-02-26 14:03
 * @see HttpExchange
 **/
public class HttpExchangeMetaFactory extends AbstractAnnotationMetaFactory<HttpExchange, HttpExchangeMetaFactory.HttpExchangeMate> {

    @Override
    public HttpExchangeMate factory(HttpExchange annotation) {
        return super.newProxyMate(annotation, HttpExchangeMetaFactory.HttpExchangeMate.class);
    }

    public abstract static class HttpExchangeMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, HttpExchange {

    }
}
