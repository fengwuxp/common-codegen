package com.wuxp.codegen.model.languages.typescript;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.enums.ClassType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 主要用定义 typescript 原本支持的类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public final class TypescriptClassMeta extends CommonCodeGenClassMeta {

    //object
    public final static TypescriptClassMeta OBJECT = new TypescriptClassMeta("object", null, ClassType.CLASS, false, null);

    public final static TypescriptClassMeta ANY = new TypescriptClassMeta("any", "any", ClassType.CLASS, false, null);

    //string
    public final static TypescriptClassMeta STRING = new TypescriptClassMeta("string", null, ClassType.CLASS, false, OBJECT);

    //number
    public final static TypescriptClassMeta NUMBER = new TypescriptClassMeta("number", null, ClassType.CLASS, false, OBJECT);

    //boolean
    public final static TypescriptClassMeta BOOLEAN = new TypescriptClassMeta("boolean", null, ClassType.CLASS, false, OBJECT);

    //array
    public final static TypescriptClassMeta ARRAY = new TypescriptClassMeta("Array", "Array<T>", ClassType.CLASS, false, OBJECT);

    //map
    public final static TypescriptClassMeta MAP = new TypescriptClassMeta("Map", "Map<K,V>", ClassType.CLASS, false, OBJECT);

    //set
    public final static TypescriptClassMeta SET = new TypescriptClassMeta("Set", "Set<V>", ClassType.CLASS, false, OBJECT);


    //promise
    public final static TypescriptClassMeta PROMISE = new TypescriptClassMeta("Promise", "Promise<V>", ClassType.CLASS, false, OBJECT);

    //regExp
    public final static TypescriptClassMeta REGEXP = new TypescriptClassMeta("RegExp", null, ClassType.CLASS, false, OBJECT);

    //Date
    public final static TypescriptClassMeta DATE = new TypescriptClassMeta("Date", null, ClassType.CLASS, false, OBJECT);


    //void
    public final static TypescriptClassMeta VOID = new TypescriptClassMeta("void", null, ClassType.CLASS, false, null);


    //type variable 类型变量
    public final static TypescriptClassMeta TYPE_VARIABLE = new TypescriptClassMeta("T", "T", ClassType.CLASS, false, OBJECT);

    public TypescriptClassMeta() {
    }

    private TypescriptClassMeta(String name, String genericDescription, ClassType classType, Boolean isAbstract, TypescriptClassMeta superClass) {
        this.name = name;
        this.genericDescription = genericDescription;
        this.classType = classType;
        this.isAbstract = isAbstract;
        this.superClass = superClass;
    }
}
