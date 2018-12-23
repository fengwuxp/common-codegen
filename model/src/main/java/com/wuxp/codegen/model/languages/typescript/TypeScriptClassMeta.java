package com.wuxp.codegen.model.languages.typescript;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.enums.ClassType;


/**
 * 主要用定义 typescript 原本支持的类
 */
public final class TypeScriptClassMeta extends CommonCodeGenClassMeta {

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


    private TypeScriptClassMeta(String name, String genericName, ClassType classType, Boolean isAbstract, TypeScriptClassMeta superClass) {
        this.genericDescription = genericName;
        this.classType = classType;
        this.isAbstract = isAbstract;
        this.superClass = superClass;
    }
}
