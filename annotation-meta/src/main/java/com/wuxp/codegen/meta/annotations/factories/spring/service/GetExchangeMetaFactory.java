package com.wuxp.codegen.meta.annotations.factories.spring.service;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.service.annotation.GetExchange;

/**
 * @author wuxp
 * @date 2026-02-26 14:03
 * @see GetExchange
 **/
public class GetExchangeMetaFactory extends AbstractAnnotationMetaFactory<GetExchange, GetExchangeMetaFactory.GetExchangeMate> {

    @Override
    public GetExchangeMate factory(GetExchange annotation) {
        return super.newProxyMate(annotation, GetExchangeMetaFactory.GetExchangeMate.class);
    }

    public abstract static class GetExchangeMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, GetExchange {

    }
}
