package com.wuxp.codegen.model;

import com.wuxp.codegen.model.enums.ClassType;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
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


    public String getGenericDescription() {
        if (!StringUtils.hasText(genericDescription)) {
            if (this.typeVariables != null && this.typeVariables.length > 0) {
                String typeDesc = Arrays.stream(this.typeVariables)
                        .map(CommonCodeGenClassMeta::getGenericDescription)
                        .collect(Collectors.joining(","));
                this.genericDescription = this.name + "<" + typeDesc + ">";
            }
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
            return this.getGenericDescription();
        }
        return this.name;
    }

    /**
     * 获取超类的最终名称
     * @param supperName
     * @return
     */
//    public String getSupperClassFinallyName(String supperName) {
//        if (this.superTypeVariables == null) {
//            return null;
//        }
//        CommonCodeGenClassMeta[] commonCodeGenClassMetas = this.superTypeVariables.get(supperName);
//        if (commonCodeGenClassMetas == null) {
//            return null;
//        }
//        String typeDesc = Arrays.stream(commonCodeGenClassMetas).map(CommonBaseMeta::getName)
//                .collect(Collectors.joining(","));
//        return null;
//    }
}
