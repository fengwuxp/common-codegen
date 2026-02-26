package com.wuxp.codegen.meta.annotations.factories.spring.service;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.service.annotation.PutExchange;

/**
 * @author wuxp
 * @date 2026-02-26 14:03
 * @see org.springframework.web.service.annotation.PutExchange
 **/
public class PutExchangeMetaFactory extends AbstractAnnotationMetaFactory<PutExchange, PutExchangeMetaFactory.PutExchangeMate> {

    @Override
    public PutExchangeMate factory(PutExchange annotation) {
        return super.newProxyMate(annotation, PutExchangeMate.class);
    }

    public abstract static class PutExchangeMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, PutExchange {

    }
}
