package com.wuxp.codegen.meta.annotations.factories;

import com.wuxp.codegen.meta.util.CodegenAnnotationUtils;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import lombok.Getter;
import org.jspecify.annotations.Nullable;


import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public abstract class AbstractAnnotationMate implements AnnotationMate {

    @Getter
    private final Map<String, Object> attributes;

    private final Map<String, Object> defaultAttributes;

    protected AbstractAnnotationMate() {
        this.attributes = CodegenAnnotationUtils.getAnnotationAttributes(this);
        this.defaultAttributes = CodegenAnnotationUtils.getDefaultAnnotationAttributes(this.annotationType());
    }

    @Override
    public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
        return getDefaultAnnotation(ElementType.PARAMETER);
    }

    @Override
    public CommonCodeGenAnnotation toAnnotation(Field annotationOwner) {
        return getDefaultAnnotation(ElementType.FIELD);
    }

    protected CommonCodeGenAnnotation getDefaultAnnotation(ElementType type) {
        CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
        annotation.setName(getSimpleName());
        annotation.setNamedArguments(getNamedArguments());
        annotation.setElementType(type);
        return annotation;
    }

    protected String getSimpleName() {
        return annotationType().getSimpleName();
    }

    protected Map<String, String> getNamedArguments() {
        Map<String, String> result = new TreeMap<>();
        CodegenAnnotationUtils.getAndQuoteStringTypeAttributes(this).forEach((key, val) -> {
            if (val != null) {
                result.put(key, val);
            }
        });
        return result;
    }

    @Nullable
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Nullable
    public String getAttributeAsString(String name) {
        return (String) attributes.get(name);
    }

    @Nullable
    public Object getAttributeIgnoreDefault(String name) {
        Object result = getAttribute(name);
        if (Objects.equals(result, defaultAttributes.get(name))) {
            return null;
        }
        return result;
    }

    protected String getAttributeSafeToString(String name) {
        Object val = getAttributeIgnoreDefault(name);
        return val == null ? "" : String.valueOf(val);
    }
}
