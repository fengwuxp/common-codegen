package com.wuxp.codegen.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 通用的代码生成注解描述元数据
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class CommonCodeGenAnnotation extends CommonBaseMeta {

    /**
     * 原目标 Annotation
     */
    private Annotation source;

    /**
     * 支持的类型
     */
    private ElementType elementType;

    /**
     * 原始注解持有者
     */
    private AnnotatedElement annotationOwner;

    /**
     * 命名参数
     */
    private Map<String, String> namedArguments;

    /**
     * 位置参数
     */
    private List<String> positionArguments;

    /**
     * 由于将java相关注解转换为其他注解的时候可能需要装换成多个次要的注解放在这个字段中
     */
    private List<CommonCodeGenAnnotation> associatedAnnotations;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        CommonCodeGenAnnotation that = (CommonCodeGenAnnotation) o;
        return Objects.equals(namedArguments, that.namedArguments) &&
                Objects.equals(positionArguments, that.positionArguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), namedArguments, positionArguments);
    }
}
