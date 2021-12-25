package com.wuxp.codegen.core;


import org.springframework.util.StringUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 代码生成用于提取注释
 *
 * @author wuxp
 * @since 1.1.0
 */
public interface CodeGenCommentExtractor {

    /**
     * 分隔多行注释的标记
     * 如果需要返回注释可以使用其将注释连接
     *
     * @see #toComments(AnnotatedElement)
     */
    String MULTILINE_COMMENT_TAG = "___________________________multiline_tag_@@@@@@@@@@_end___________________________";


    /**
     * 生成注释
     *
     * @param element 带注释的element
     * @return 从element得到的注释列表
     */
    default List<String> toComments(AnnotatedElement element) {

        String comment = toComment(element);

        if (comment == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(comment.split(MULTILINE_COMMENT_TAG))
                .filter(StringUtils::hasText)
//                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 生成注释
     *
     * @param element 带注释的element
     * @return 从element得到的注释
     */
    default String toComment(AnnotatedElement element) {
        if (element == null) {
            return null;
        }
        if (element instanceof Class) {
            return this.toComment((Class<?>) element);
        } else if (element instanceof Field) {
            return this.toComment((Field) element);
        } else if (element instanceof Parameter) {
            return this.toComment((Parameter) element);
        } else {
            return this.toComment((Method) element);
        }
    }

    /**
     * 转换为注释
     *
     * @param clazz 类对象
     * @return 从owner得到的注释
     */
    default String toComment(Class<?> clazz) {
        return null;
    }


    /**
     * 转换为注释
     *
     * @param field 成员字段对象
     * @return 从owner得到的注释
     */
    default String toComment(Field field) {
        return null;
    }

    /**
     * 转换为注释
     *
     * @param parameter 参数对象
     * @return 从owner得到的注释
     */
    default String toComment(Parameter parameter) {
        return null;
    }

    /**
     * 转换为注释
     *
     * @param method 方法对象
     * @return 从owner得到的注释
     */
    default String toComment(Method method) {
        return null;
    }
}
