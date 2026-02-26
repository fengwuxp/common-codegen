package com.wuxp.codegen.meta.annotations.factories.spring.service;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.service.annotation.DeleteExchange;

/**
 * @author wuxp
 * @date 2026-02-26 14:03
 * @see DeleteExchange
 **/
public class DeleteExchangeMetaFactory extends AbstractAnnotationMetaFactory<DeleteExchange, DeleteExchangeMetaFactory.DeleteExchangeMate> {

    @Override
    public DeleteExchangeMate factory(DeleteExchange annotation) {
        return super.newProxyMate(annotation, DeleteExchangeMate.class);
    }

    public abstract static class DeleteExchangeMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, DeleteExchange {

    }
}
