package com.wuxp.codegen.model.mapping;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 基础数据的类型映射
 *
 * @author wuxp
 */
public class BaseTypeMapping<T extends CommonCodeGenClassMeta> implements TypeMapping<Class<?>, T> {

    private final Map<Class<?>, CommonCodeGenClassMeta> typeMapping;

    /**
     * 时间类型 希望装换的目标类型
     */
    private final T dateToClassTarget;

    public BaseTypeMapping(Map<Class<?>, CommonCodeGenClassMeta> typeMapping) {
        this(typeMapping, null);
    }

    public BaseTypeMapping(Map<Class<?>, CommonCodeGenClassMeta> typeMapping, T dateToClassTarget) {
        this.typeMapping = typeMapping;
        this.dateToClassTarget = dateToClassTarget;
    }

    @Override
    public T mapping(Class<?>... classes) {
        if (classes == null) {
            return null;
        }
        Class<?> clazz = classes[0];
        if (JavaTypeUtils.isDate(clazz) && this.dateToClassTarget != null) {
            return this.dateToClassTarget;
        }
        return (T) this.typeMapping.get(clazz);
    }

    /**
     * 尝试设置，如果已经存在则不处理
     *
     * @param clazzType java 类型
     * @param classMeta 用于生成的其他语言类型描述
     * @return
     */
    public BaseTypeMapping<T> tryAddTypeMapping(Class<?> clazzType, CommonCodeGenClassMeta classMeta) {
        if (classMeta == null) {
            return this;
        }
        Assert.notNull(clazzType, "mapping class is not null");
        if (!typeMapping.containsKey(clazzType)) {
            typeMapping.put(clazzType, classMeta);
        }
        return this;
    }

    /**
     * 强制设置
     *
     * @param clazzType java 类型
     * @param classMeta 用于生成的其他语言类型描述
     * @return
     */
    public BaseTypeMapping<T> forceAddTypeMapping(Class<?> clazzType, CommonCodeGenClassMeta classMeta) {
        if (classMeta == null) {
            return this;
        }
        Assert.notNull(clazzType, "mapping class is not null");
        typeMapping.put(clazzType, classMeta);
        return this;
    }
}
