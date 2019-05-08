package com.wuxp.codegen.model.languages.java.codegen;


import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.enums.ClassType;


/**
 * Java 代码生成的类型常量
 */
public final class JavaCodeGenClassMeta extends CommonCodeGenClassMeta {


    /**
     * Byte
     */
    public static final JavaCodeGenClassMeta BYTE = new JavaCodeGenClassMeta("Byte", null, ClassType.CLASS, false, null, "", false);


    /**
     * Short
     */
    public static final JavaCodeGenClassMeta SHORT = new JavaCodeGenClassMeta("Short", null, ClassType.CLASS, false, null, "", false);


    /**
     * Integer
     */
    public static final JavaCodeGenClassMeta INTEGER = new JavaCodeGenClassMeta("Integer", null, ClassType.CLASS, false, null, "", false);

    /**
     * Long
     */
    public static final JavaCodeGenClassMeta LONG = new JavaCodeGenClassMeta("Long", null, ClassType.CLASS, false, null, "", false);

    /**
     * Double
     */
    public static final JavaCodeGenClassMeta DOUBLE = new JavaCodeGenClassMeta("Double", null, ClassType.CLASS, false, null, "", false);

    /**
     * Float
     */
    public static final JavaCodeGenClassMeta FLOAT = new JavaCodeGenClassMeta("Float", null, ClassType.CLASS, false, null, "", false);


    /**
     * BigDecimal
     */
    public static final JavaCodeGenClassMeta BIG_DECIMAL = new JavaCodeGenClassMeta("BigDecimal", null, ClassType.CLASS, false, null, "java.math.BigDecimal", true);

    /**
     * Number
     */
    public static final JavaCodeGenClassMeta NUMBER = new JavaCodeGenClassMeta("Number", null, ClassType.CLASS, false, null, "", false);

    /**
     * Boolean
     */
    public static final JavaCodeGenClassMeta BOOLEAN = new JavaCodeGenClassMeta("Boolean", null, ClassType.CLASS, false, null, "", false);

    /**
     * char
     */
    public static final JavaCodeGenClassMeta CHAR = new JavaCodeGenClassMeta("char", null, ClassType.CLASS, false, null, "", false);


    /**
     * CharSequence
     */
    public static final JavaCodeGenClassMeta CHAR_SEQUENCE = new JavaCodeGenClassMeta("CharSequence", null, ClassType.CLASS, false, null, "", false);

    /**
     * String
     */
    public static final JavaCodeGenClassMeta STRING = new JavaCodeGenClassMeta("String", null, ClassType.CLASS, false, null, "", false);

    /**
     * Date
     */
    public static final JavaCodeGenClassMeta DATE = new JavaCodeGenClassMeta("Date", null, ClassType.CLASS, false, null, "java.util.Date", true);

    /**
     * Map
     */
    public static final JavaCodeGenClassMeta MAP = new JavaCodeGenClassMeta("Map", "Map<K,V>", ClassType.CLASS, false, null, "java.util.Map", true);

    /**
     * Set
     */
    public static final JavaCodeGenClassMeta SET = new JavaCodeGenClassMeta("Set", "Set<V>", ClassType.CLASS, false, null, "java.util.Set", true);

    /**
     * List
     */
    public static final JavaCodeGenClassMeta LIST = new JavaCodeGenClassMeta("List", "List<T>", ClassType.CLASS, false, null, "java.util.List", true);


    /**
     * Collection
     */
    public static final JavaCodeGenClassMeta COLLECTION = new JavaCodeGenClassMeta("Collection", "Collection<T>", ClassType.CLASS, false, null, "java.util.Collection", true);


    /**
     * Object
     */
    public static final JavaCodeGenClassMeta OBJECT = new JavaCodeGenClassMeta("Object", null, ClassType.CLASS, false, null, "", false);


    /**
     * void
     */
    public static final JavaCodeGenClassMeta VOID = new JavaCodeGenClassMeta("void", null, ClassType.CLASS, false, null, "", false);


    //type variable 类型变量
    public final static JavaCodeGenClassMeta TYPE_VARIABLE = new JavaCodeGenClassMeta("T", "T", ClassType.CLASS, false, OBJECT, "", false);


    //Observable
    public static final JavaCodeGenClassMeta RX_JAVA2_OBSERVABLE = new JavaCodeGenClassMeta("Observable", "T", ClassType.CLASS, false, null, "", false);


    public JavaCodeGenClassMeta() {
    }

    public JavaCodeGenClassMeta(String name, String genericDescription, ClassType classType, Boolean isAbstract, CommonCodeGenClassMeta superClass, String packagePath, Boolean needImport) {
        super(name, genericDescription, classType, isAbstract, superClass, packagePath, needImport);
    }
}
