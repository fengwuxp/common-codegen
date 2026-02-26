package com.wuxp.codegen.meta.annotations.factories.spring.service;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.service.annotation.PatchExchange;

/**
 * @author wuxp
 * @date 2026-02-26 14:03
 * @see PatchExchange
 **/
public class PatchExchangeMetaFactory extends AbstractAnnotationMetaFactory<PatchExchange, PatchExchangeMetaFactory.PatchExchangeMate> {

    @Override
    public PatchExchangeMate factory(PatchExchange annotation) {
        return super.newProxyMate(annotation, PatchExchangeMate.class);
    }

    public abstract static class PatchExchangeMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, PatchExchange {

    }
}
