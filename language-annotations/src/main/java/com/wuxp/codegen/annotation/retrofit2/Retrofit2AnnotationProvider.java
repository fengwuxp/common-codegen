package com.wuxp.codegen.annotation.retrofit2;

import com.wuxp.codegen.annotation.AbstractClientAnnotationProvider;
import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuxp
 */
public class Retrofit2AnnotationProvider extends AbstractClientAnnotationProvider {

    {
        this.annotationMap.put(RequestMapping.class, Retrofit2RequestMappingMate.class);
    }

    public Retrofit2AnnotationProvider() {
        super(new ConcurrentHashMap<>(8));
    }

    public abstract static class Retrofit2RequestMappingMate extends RequestMappingProcessor.RequestMappingMate {

    }

}

