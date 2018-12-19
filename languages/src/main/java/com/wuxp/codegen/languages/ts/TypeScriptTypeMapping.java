package com.wuxp.codegen.languages.ts;

import com.wuxp.codegen.core.mapping.BaseTypeMapping;
import com.wuxp.codegen.core.mapping.TypeMapping;
import com.wuxp.codegen.core.utils.JavaTypeUtil;
import com.wuxp.codegen.model.languages.typescript.TypeScriptClassMeta;

import java.lang.reflect.Type;
import java.util.*;


/**
 * typeScript 的类型映射
 */
public class TypeScriptTypeMapping implements TypeMapping<TypeScriptClassMeta> {

    private static final Map<Type, TypeScriptClassMeta> TYPE_MAPPING = new HashMap<>();

    static {
        TYPE_MAPPING.put(Object.class, TypeScriptClassMeta.OBJECT);
        TYPE_MAPPING.put(void.class, TypeScriptClassMeta.VOID);
        TYPE_MAPPING.put(Number.class, TypeScriptClassMeta.NUMBER);
        TYPE_MAPPING.put(byte.class, TypeScriptClassMeta.NUMBER);
        TYPE_MAPPING.put(short.class, TypeScriptClassMeta.NUMBER);
        TYPE_MAPPING.put(int.class, TypeScriptClassMeta.NUMBER);
        TYPE_MAPPING.put(float.class, TypeScriptClassMeta.NUMBER);
        TYPE_MAPPING.put(double.class, TypeScriptClassMeta.NUMBER);
        TYPE_MAPPING.put(long.class, TypeScriptClassMeta.NUMBER);
        TYPE_MAPPING.put(Boolean.class, TypeScriptClassMeta.BOOLEAN);
        TYPE_MAPPING.put(boolean.class, TypeScriptClassMeta.BOOLEAN);
        TYPE_MAPPING.put(String.class, TypeScriptClassMeta.STRING);
        TYPE_MAPPING.put(char.class, TypeScriptClassMeta.STRING);
        TYPE_MAPPING.put(Map.class, TypeScriptClassMeta.MAP);
        TYPE_MAPPING.put(List.class, TypeScriptClassMeta.ARRAY);
        TYPE_MAPPING.put(Set.class, TypeScriptClassMeta.SET);
        TYPE_MAPPING.put(Date.class, TypeScriptClassMeta.DATE);
    }

    private TypeMapping<TypeScriptClassMeta> baseTypeMapping = new BaseTypeMapping<TypeScriptClassMeta>(TYPE_MAPPING);

    //时间类型 希望装换的目标类型
    private TypeScriptClassMeta dateToClassTarget = TypeScriptClassMeta.DATE;


    @Override
    public TypeScriptClassMeta mapping(Type[] classes) {

        if (classes == null) {
            return null;
        }
        Class<?> clazz = (Class<?>) classes[0];
        if (JavaTypeUtil.isJavaBaseType(clazz)) {
            if (JavaTypeUtil.isNumber(clazz)) {
                return this.baseTypeMapping.mapping(new Type[]{Number.class});
            }
            if (JavaTypeUtil.isDate(clazz)) {
                return this.dateToClassTarget;
            }
            return this.baseTypeMapping.mapping(classes);
        }

        //类型处理


        return null;
    }
}
