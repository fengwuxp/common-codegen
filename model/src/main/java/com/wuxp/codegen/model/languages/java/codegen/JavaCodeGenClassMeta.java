package com.wuxp.codegen.model.languages.java.codegen;


import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.util.SnowFlakeIdGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * Java 代码生成的类型常量
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
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
    public static final JavaCodeGenClassMeta BIG_DECIMAL = new JavaCodeGenClassMeta("BigDecimal", null, ClassType.CLASS, false, null,
            "java.math.BigDecimal", true);

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
    public static final JavaCodeGenClassMeta CHAR_SEQUENCE = new JavaCodeGenClassMeta("CharSequence", null, ClassType.CLASS, false, null, "",
            false);

    /**
     * String
     */
    public static final JavaCodeGenClassMeta STRING = new JavaCodeGenClassMeta("String", null, ClassType.CLASS, false, null, "", false);

    /**
     * Date
     */
    public static final JavaCodeGenClassMeta DATE = new JavaCodeGenClassMeta("Date", null, ClassType.CLASS, false, null, "java.util.Date",
            true);

    /**
     * Map
     */
    public static final JavaCodeGenClassMeta MAP = new JavaCodeGenClassMeta("Map", "Map<K,V>", ClassType.INTERFACE, false, null,
            "java.util.Map", true);

    /**
     * Set
     */
    public static final JavaCodeGenClassMeta SET = new JavaCodeGenClassMeta("Set", "Set<V>", ClassType.INTERFACE, false, null,
            "java.util.Set", true);

    /**
     * List
     */
    public static final JavaCodeGenClassMeta LIST = new JavaCodeGenClassMeta("List", "List<T>", ClassType.INTERFACE, false, null,
            "java.util.List", true);


    /**
     * Collection
     */
    public static final JavaCodeGenClassMeta COLLECTION = new JavaCodeGenClassMeta("Collection", "Collection<T>", ClassType.INTERFACE, false,
            null, "java.util.Collection", true);

    /**
     * File
     */
    public static final JavaCodeGenClassMeta FILE = new JavaCodeGenClassMeta("File", null, ClassType.CLASS, false, null, "java.io.File",
            true);


    /**
     * Object
     */
    public static final JavaCodeGenClassMeta OBJECT = new JavaCodeGenClassMeta("Object", "Object", ClassType.CLASS, false, null, "", false);


    /**
     * void
     */
    public static final JavaCodeGenClassMeta VOID = new JavaCodeGenClassMeta("void", null, ClassType.CLASS, false, null, "", false);


    //type variable 类型变量
    public final static JavaCodeGenClassMeta TYPE_VARIABLE = new JavaCodeGenClassMeta("T", "N", ClassType.CLASS, false, OBJECT, "", false);


    //Observable
    public static final JavaCodeGenClassMeta RX_JAVA2_OBSERVABLE = new JavaCodeGenClassMeta("Observable", "Observable<T>", ClassType.CLASS,
            false, null, "", false);

    // java数组类型标记
    public static final JavaCodeGenClassMeta JAVA_ARRAY_CLASS_TYPE_MARK = new JavaCodeGenClassMeta("JavaArrayClassTypeMark", null,
            ClassType.CLASS, false, null, "", false);


    // or use 1L?
    private Long serialVersionUIDValue = SnowFlakeIdGenerator.SINGLETON.nextId();

    public JavaCodeGenClassMeta() {
    }

    public JavaCodeGenClassMeta(String name, String genericDescription, ClassType classType, Boolean isAbstract,
                                CommonCodeGenClassMeta superClass, String packagePath, Boolean needImport) {
        super(name, genericDescription, classType, isAbstract, superClass, packagePath, needImport, false);
    }
}
