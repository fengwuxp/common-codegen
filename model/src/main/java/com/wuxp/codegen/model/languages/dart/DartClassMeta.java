package com.wuxp.codegen.model.languages.dart;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.enums.ClassType;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 主要用定义 dart 原本支持的类
 *
 * @author wxup
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DartClassMeta extends CommonCodeGenClassMeta {

    public static final String BUILT_COLLECTION_PATH = "package:built_collection/built_collection.dart";

    /**
     * Object
     */
    public static final DartClassMeta OBJECT = new DartClassMeta("Object", null, ClassType.CLASS, false, null);
    public static final DartClassMeta DYNAMIC = new DartClassMeta("dynamic", null, ClassType.CLASS, false, null);

    /**
     * String
     */
    public static final DartClassMeta STRING = new DartClassMeta("String", null, ClassType.CLASS, false, null);

    /**
     * Number
     */
    public static final DartClassMeta INT = new DartClassMeta("int", null, ClassType.CLASS, false, null);
    public static final DartClassMeta DOUBLE = new DartClassMeta("double", null, ClassType.CLASS, false, null);
    public static final DartClassMeta NUM = new DartClassMeta("num", null, ClassType.CLASS, false, null);

    /**
     * boolean
     */
    public static final DartClassMeta BOOL = new DartClassMeta("bool", null, ClassType.CLASS, false, null);

    /**
     * List
     */
    public static final DartClassMeta LIST = new DartClassMeta("List", "List<T>", ClassType.CLASS, false, OBJECT);
    public static final DartClassMeta BUILT_LIST = new DartClassMeta("BuiltList", "BuiltList<T>", ClassType.CLASS, false, OBJECT,
            BUILT_COLLECTION_PATH);

    /**
     * Map
     */
    public static final DartClassMeta MAP = new DartClassMeta("Map", "Map<K,V>", ClassType.CLASS, false, OBJECT);
    public static final DartClassMeta BUILT_MAP = new DartClassMeta("BuiltMap", "BuiltMap<K,V>", ClassType.CLASS, false, OBJECT,
            BUILT_COLLECTION_PATH);

    /**
     * Set
     */
    public static final DartClassMeta SET = new DartClassMeta("Set", "Set<V>", ClassType.CLASS, false, OBJECT);
    public static final DartClassMeta BUILT_SET = new DartClassMeta("BuiltSet", "BuiltSet<V>", ClassType.CLASS, false, OBJECT,
            BUILT_COLLECTION_PATH);

    /**
     * Iterable
     */
    public static final DartClassMeta ITERABLE = new DartClassMeta("Iterable", "Iterable<T>", ClassType.CLASS, false, OBJECT);
    public static final DartClassMeta BUILT_ITERABLE = new DartClassMeta("BuiltIterable", "BuiltIterable<T>", ClassType.CLASS, false, OBJECT,
            BUILT_COLLECTION_PATH);


    /**
     * Serializers
     */
    public static final DartClassMeta BUILT_SERIALIZERS = new DartClassMeta("Serializers", null, ClassType.CLASS, false, OBJECT,
            "/serializers");


    /**
     * Future
     */
    public static final DartClassMeta FUTURE = new DartClassMeta("Future", "Future<V>", ClassType.CLASS, false, OBJECT);

    /**
     * RegExp
     */
    public static final DartClassMeta REGEXP = new DartClassMeta("RegExp", null, ClassType.CLASS, false, OBJECT);

    /**
     * Date
     */
    public static final DartClassMeta DATE = new DartClassMeta("DateTime", null, ClassType.CLASS, false, OBJECT);


    /**
     * Void
     */
    public static final DartClassMeta VOID = new DartClassMeta("void", null, ClassType.CLASS, false, null);

    /**
     * File
     */
    public static final DartClassMeta FILE = new DartClassMeta("File", null, ClassType.CLASS, false, null);


    public DartClassMeta() {
    }

    public DartClassMeta(String name,
                         String genericDescription,
                         ClassType classType,
                         Boolean isAbstract,
                         DartClassMeta superClass,
                         String packagePath) {
        this(name, genericDescription, classType, isAbstract, superClass);
        this.packagePath = packagePath;
        this.needImport = true;
    }

    private DartClassMeta(String name,
                          String genericDescription,
                          ClassType classType,
                          Boolean isAbstract,
                          DartClassMeta superClass) {
        this.name = name;
        this.genericDescription = genericDescription;
        this.classType = classType;
        this.isAbstract = isAbstract;
        this.superClass = superClass;
        this.needGenerate = false;
        this.needImport = false;
    }

    public static DartClassMeta newInstance(String name,
                                            String genericDescription,
                                            ClassType classType,
                                            Boolean isAbstract,
                                            DartClassMeta superClass,
                                            String packagePath) {
        return new DartClassMeta(name, genericDescription, classType, isAbstract, superClass, packagePath);
    }
}
