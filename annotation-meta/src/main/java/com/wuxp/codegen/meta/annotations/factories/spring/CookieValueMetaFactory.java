package com.wuxp.codegen.meta.annotations.factories.spring;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.bind.annotation.CookieValue;

import java.lang.reflect.Parameter;


/**
 * @author wxup
 * @see CookieValue
 * 处理 CookieValue 注解
 */
public class CookieValueMetaFactory extends AbstractAnnotationMetaFactory<CookieValue, CookieValueMetaFactory.CookieValueMate> {


    @Override
    public CookieValueMetaFactory.CookieValueMate factory(CookieValue annotation) {

        return super.newProxyMate(annotation, CookieValueMetaFactory.CookieValueMate.class);
    }


    public abstract static class CookieValueMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, CookieValue {

        @Override
        public String toComment(Parameter annotationOwner) {
            return String.format("参数：%s 是一个 Cookie Value，%s，默认值：%s", this.getParameterName(annotationOwner),
                    required() ? "必填" : "非必填",
                    getAttributeSafeToString(ANNOTATION_DEFAULT_VALUE_KEY));
        }
    }
}
