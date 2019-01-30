package com.wuxp.codegen.dragon.swagger2.annotations;

import com.wuxp.codegen.dragon.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.dragon.annotation.processor.AnnotationMate;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * swagger2 注解处理
 *
 * @see ApiModelProperty
 */
public class ApiModelPropertyProcessor extends AbstractAnnotationProcessor<ApiModelProperty, ApiModelPropertyProcessor.ApiModelPropertyMate> {


    @Override
    public ApiModelPropertyMate process(ApiModelProperty annotation) {
        return this.newProxyMate(annotation, ApiModelPropertyMate.class);
    }


    public abstract static class ApiModelPropertyMate implements AnnotationMate<ApiModelProperty>, ApiModelProperty {


        @Override
        public String toComment(Field annotationOwner) {
            String fieldDescription;
            String notes = this.notes();
            String description = StringUtils.hasText(notes) ? notes : this.value();
            if (annotationOwner.isEnumConstant()) {
                //是枚举的字段
                fieldDescription = description;
            } else {
                fieldDescription = String.format("属性说明：%s，示例输入：%s", description,this.example());
            }

            return fieldDescription;
        }
    }
}
