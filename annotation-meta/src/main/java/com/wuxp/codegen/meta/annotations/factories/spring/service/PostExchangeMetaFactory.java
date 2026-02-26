package com.wuxp.codegen.meta.annotations.factories.spring.service;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wuxp
 * @date 2026-02-26 14:03
 * @see PostExchange
 **/
public class PostExchangeMetaFactory extends AbstractAnnotationMetaFactory<PostExchange, PostExchangeMetaFactory.PostExchangeMate> {

    @Override
    public PostExchangeMate factory(PostExchange annotation) {
        return super.newProxyMate(annotation, PostExchangeMate.class);
    }

    public abstract static class PostExchangeMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, PostExchange {

    }
}
