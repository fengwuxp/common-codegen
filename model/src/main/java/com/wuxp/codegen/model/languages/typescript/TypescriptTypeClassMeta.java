package com.wuxp.codegen.model.languages.typescript;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.enums.ClassType;


/**
 * 主要用定义 typescript 原本支持的类
 */
public final class TypescriptTypeClassMeta extends CommonCodeGenClassMeta {

    //object
    public final static TypescriptTypeClassMeta OBJECT = new TypescriptTypeClassMeta("object", null, ClassType.CLASS, false, null);

    //string
    public final static TypescriptTypeClassMeta STRING = new TypescriptTypeClassMeta("string", null, ClassType.CLASS, false, OBJECT);

    //number
    public final static TypescriptTypeClassMeta NUMBER = new TypescriptTypeClassMeta("number", null, ClassType.CLASS, false, OBJECT);

    //boolean
    public final static TypescriptTypeClassMeta BOOLEAN = new TypescriptTypeClassMeta("boolean", null, ClassType.CLASS, false, OBJECT);

    //array
    public final static TypescriptTypeClassMeta ARRAY = new TypescriptTypeClassMeta("Array", "Array<T>", ClassType.CLASS, false, OBJECT);

    //map
    public final static TypescriptTypeClassMeta MAP = new TypescriptTypeClassMeta("Map", "Map<K,V>", ClassType.CLASS, false, OBJECT);

    //set
    public final static TypescriptTypeClassMeta SET = new TypescriptTypeClassMeta("Set", "Set<V>", ClassType.CLASS, false, OBJECT);

    //regExp
    public final static TypescriptTypeClassMeta REGEXP = new TypescriptTypeClassMeta("RegExp", null, ClassType.CLASS, false, OBJECT);

    //regExp
    public final static TypescriptTypeClassMeta DATE = new TypescriptTypeClassMeta("date", null, ClassType.CLASS, false, OBJECT);


    //void
    public final static TypescriptTypeClassMeta VOID = new TypescriptTypeClassMeta("void", null, ClassType.CLASS, false, null);


    private TypescriptTypeClassMeta(String name, String genericName, ClassType classType, Boolean isAbstract, TypescriptTypeClassMeta superClass) {
        this.genericDescription = genericName;
        this.classType = classType;
        this.isAbstract = isAbstract;
        this.superClass = superClass;
    }
}
