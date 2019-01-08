package com.wuxp.codegen.model;

import com.wuxp.codegen.model.enums.ClassType;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用的代码生成 class 元数据
 */
@Data
public class CommonCodeGenClassMeta extends CommonBaseMeta {


    /**
     * 原目标类
     */
    protected Class<?> source;


    /**
     * 类类型
     */
    protected ClassType classType;

    /**
     * 是否为抽象的
     */
    protected Boolean isAbstract;

    /**
     * 超类
     */
    protected CommonCodeGenClassMeta superClass;

    /**
     * 实现的接口
     */
    protected CommonCodeGenClassMeta[] interfaces;

    /**
     * 注解
     */
    protected CommonCodeGenAnnotation[] annotations;

    /**
     * 包所在路径
     */
    protected String packagePath;

    /**
     * 依赖
     */
    protected Map<String, ? extends CommonCodeGenClassMeta> dependencies;

    /**
     * 在有泛型时候的描述
     * 例如 Set<T>,Map<K,V> 等
     */
    protected String genericDescription;


    /**
     * 方法列表
     */
    protected CommonCodeGenMethodMeta[] methodMetas;


    /**
     * 属性列表
     */
    protected CommonCodeGenFiledMeta[] filedMetas;


    public String getGenericDescription() {
        if (!StringUtils.hasText(genericDescription)) {
            if (this.typeVariables != null && this.typeVariables.length > 0) {
                String typeDesc = Arrays.stream(this.typeVariables).map(type -> type.getTypeName()).collect(Collectors.joining(","));
                this.genericDescription = this.name + "<" + typeDesc + ">";
            }
        }
        return genericDescription;
    }
}
