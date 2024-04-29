package com.wuxp.codegen.meta.annotations.factories;

import com.wuxp.codegen.meta.util.CodegenAnnotationUtils;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.util.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractNamedAnnotationMate extends AbstractAnnotationMate implements NamedAnnotationMate {

    @Override
    public String name() {
        String name = getAttributeAsString(ANNOTATION_NAME_KEY);
        return StringUtils.hasText(name) ? name : getAttributeAsString(ANNOTATION_VALUE_KEY);
    }

    @Override
    public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
        CommonCodeGenAnnotation annotation = super.toAnnotation(annotationOwner);
        Map<String, String> namedArguments = annotation.getNamedArguments();
        // name 和 value 都有值，移除 value
        namedArguments.remove(ANNOTATION_VALUE_KEY);
        if (!StringUtils.hasText(namedArguments.get(ANNOTATION_NAME_KEY))) {
            // 如果 name 属性为空，则尝试使用参数名称
            namedArguments.put(ANNOTATION_NAME_KEY, CodegenAnnotationUtils.quote(getParameterName(annotationOwner)));
        }
        // 注解位置参数
        List<String> positionArguments = new LinkedList<>(namedArguments.values());
        annotation.setPositionArguments(positionArguments);
        annotation.setElementType(ElementType.PARAMETER);
        return annotation;
    }


}
