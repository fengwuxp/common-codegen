package com.wuxp.codegen.model.languages.typescript;

import com.wuxp.codegen.model.enums.ClassType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class TypeScriptClassMeta extends TypeScriptBaseMeta {

    //object
    public final static TypeScriptClassMeta OBJECT = new TypeScriptClassMeta("object", null, ClassType.CLASS, false, null);

    //string
    public final static TypeScriptClassMeta STRING = new TypeScriptClassMeta("string", null, ClassType.CLASS, false, OBJECT);

    //number
    public final static TypeScriptClassMeta NUMBER = new TypeScriptClassMeta("number", null, ClassType.CLASS, false, OBJECT);

    //boolean
    public final static TypeScriptClassMeta BOOLEAN = new TypeScriptClassMeta("boolean", null, ClassType.CLASS, false, OBJECT);

    //array
    public final static TypeScriptClassMeta ARRAY = new TypeScriptClassMeta("Array", "Array<T>", ClassType.CLASS, false, OBJECT);

    //map
    public final static TypeScriptClassMeta MAP = new TypeScriptClassMeta("Map", "Map<K,V>", ClassType.CLASS, false, OBJECT);

    //set
    public final static TypeScriptClassMeta SET = new TypeScriptClassMeta("Set", "Set<V>", ClassType.CLASS, false, OBJECT);

    //regExp
    public final static TypeScriptClassMeta REGEXP = new TypeScriptClassMeta("RegExp", null, ClassType.CLASS, false, OBJECT);

    //regExp
    public final static TypeScriptClassMeta DATE = new TypeScriptClassMeta("date", null, ClassType.CLASS, false, OBJECT);


    //void
    public final static TypeScriptClassMeta VOID = new TypeScriptClassMeta("void", null, ClassType.CLASS, false, null);


    //属性类型 如果有泛型则有多个
    private String[] types;

    //类类型 interface，class enum
    private ClassType classType;

    //是否为抽象类
    private Boolean isAbstract;

    //所在包的路径
    private String packagePath;

    //导出的内容列表
    private String[] exports;

    //存在默认导出
    private Boolean hasDefaultExport;

    //成员变量列表
    private TypeScriptFieldMeta[] fieldMetas;

    //成员方法列表
    private TypeScriptMethodMeta[] methodMetas;

    //依赖类的列表
    private Set<TypeScriptClassMeta> dependencyList;

    //继承的接口列表
    private TypeScriptClassMeta[] interfaces;

    //继承的父类
    private TypeScriptClassMeta superClass;

    //在有泛型时候的名称
    private String genericName;

    private TypeScriptClassMeta(String name, String genericName, ClassType classType, Boolean isAbstract, TypeScriptClassMeta superClass) {
        super(name);
        this.genericName = genericName;
        this.classType = classType;
        this.isAbstract = isAbstract;
        this.superClass = superClass;
    }
}
