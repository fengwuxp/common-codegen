package com.wuxp.codegen.model;

import com.wuxp.codegen.model.enums.ClassType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用的代码生成 class 元数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CommonCodeGenClassMeta extends CommonBaseMeta {


    /**
     * 原目标类
     */
    protected Class<?> source;

    /**
     * 类型参数, 泛型
     */
    protected CommonCodeGenClassMeta[] typeVariables;

    /**
     * 属性类型 如果有泛型则有多个
     */
    private Map<String/*类型，父类，接口，本身*/, CommonCodeGenClassMeta[]> superTypeVariables;

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


    /**
     * 获取最终的泛型描述
     * @return
     */
    public String getFinallyGenericDescription() {
        if (this.typeVariables != null && this.typeVariables.length > 0) {
            String typeDesc = Arrays.stream(this.typeVariables)
                    .map(CommonCodeGenClassMeta::getFinallyGenericDescription)
                    .collect(Collectors.joining(","));
            return this.name + "<" + typeDesc + ">";
        }
        return genericDescription;
    }

    /**
     * 获取最终的类名称，合并了泛型描述
     *
     * @return
     */
    public String getFinallyClassName() {
        if (this.typeVariables != null && this.typeVariables.length > 0) {
            return this.getFinallyGenericDescription();
        }
        return this.name;
    }

}
