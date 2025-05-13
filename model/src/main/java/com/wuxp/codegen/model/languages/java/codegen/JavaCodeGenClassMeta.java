package com.wuxp.codegen.model.languages.java.codegen;


import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.JavaArrayClassTypeMark;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.util.SnowFlakeIdGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.File;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Java 代码生成的类型常量
 *
 * @author wuxp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public final class JavaCodeGenClassMeta extends CommonCodeGenClassMeta {


    /**
     * Byte
     */
    public static final JavaCodeGenClassMeta BYTE = new JavaCodeGenClassMeta(Byte.class, null, "", false);


    /**
     * Short
     */
    public static final JavaCodeGenClassMeta SHORT = new JavaCodeGenClassMeta(Short.class, null, "", false);


    /**
     * Integer
     */
    public static final JavaCodeGenClassMeta INTEGER = new JavaCodeGenClassMeta(Integer.class, null, "", false);

    /**
     * Long
     */
    public static final JavaCodeGenClassMeta LONG = new JavaCodeGenClassMeta(Long.class, null, "", false);

    /**
     * Double
     */
    public static final JavaCodeGenClassMeta DOUBLE = new JavaCodeGenClassMeta(Double.class, null, "", false);

    /**
     * Float
     */
    public static final JavaCodeGenClassMeta FLOAT = new JavaCodeGenClassMeta(Float.class, null, "", false);


    /**
     * BigDecimal
     */
    public static final JavaCodeGenClassMeta BIG_DECIMAL = new JavaCodeGenClassMeta(BigDecimal.class, null, "java.math.BigDecimal", true);

    /**
     * Number
     */
    public static final JavaCodeGenClassMeta NUMBER = new JavaCodeGenClassMeta(Number.class, null, "", false);

    /**
     * Boolean
     */
    public static final JavaCodeGenClassMeta BOOLEAN = new JavaCodeGenClassMeta(Boolean.class, null, "", false);

    /**
     * char
     */
    public static final JavaCodeGenClassMeta CHAR = new JavaCodeGenClassMeta(char.class, null, "", false);


    /**
     * CharSequence
     */
    public static final JavaCodeGenClassMeta CHAR_SEQUENCE = new JavaCodeGenClassMeta(CharSequence.class, null, "", false);

    /**
     * String
     */
    public static final JavaCodeGenClassMeta STRING = new JavaCodeGenClassMeta(String.class, null, "", false);

    /**
     * Date
     */
    public static final JavaCodeGenClassMeta DATE = new JavaCodeGenClassMeta(Date.class, null, "java.util.Date", true);

    public static final JavaCodeGenClassMeta LOCAL_DATE = new JavaCodeGenClassMeta(LocalDate.class, null, "java.time.LocalDate", true);

    public static final JavaCodeGenClassMeta LOCAL_DATE_TIME = new JavaCodeGenClassMeta(LocalDateTime.class, null, "java.time.LocalDateTime", true);

    /**
     * Map
     */
    public static final JavaCodeGenClassMeta MAP = new JavaCodeGenClassMeta(Map.class, "Map<K,V>", "java.util.Map", true);

    /**
     * Set
     */
    public static final JavaCodeGenClassMeta SET = new JavaCodeGenClassMeta(Set.class, "Set<V>", "java.util.Set", true);

    /**
     * List
     */
    public static final JavaCodeGenClassMeta LIST = new JavaCodeGenClassMeta(List.class, "List<T>", "java.util.List", true);


    /**
     * Collection
     */
    public static final JavaCodeGenClassMeta COLLECTION = new JavaCodeGenClassMeta(Collection.class, "Collection<T>", "java.util.Collection", true);

    /**
     * File
     */
    public static final JavaCodeGenClassMeta FILE = new JavaCodeGenClassMeta(File.class, null, "java.io.File", true);


    /**
     * Object
     */
    public static final JavaCodeGenClassMeta OBJECT = new JavaCodeGenClassMeta(Object.class, "Object", "", false);


    /**
     * void
     */
    public static final JavaCodeGenClassMeta VOID = new JavaCodeGenClassMeta(void.class, null, "", false);

    public static final JavaCodeGenClassMeta VOID_WRAPPER = new JavaCodeGenClassMeta(Void.class, null, "", false);


    /**
     * type variable 类型变量
     */
    public static final JavaCodeGenClassMeta TYPE_VARIABLE = new JavaCodeGenClassMeta("T", "T", ClassType.CLASS, false, OBJECT, "", false);


    /**
     * RxJava2 Observable
     */
    public static final JavaCodeGenClassMeta RX_JAVA2_OBSERVABLE = new JavaCodeGenClassMeta("Observable", "Observable<T>", ClassType.CLASS,
            false, null, "io.reactivex.Observable", true);

    /**
     * reactor Flux
     */
    public static final JavaCodeGenClassMeta REACTOR_FLUX = new JavaCodeGenClassMeta("Flux", "Flux<T>", ClassType.CLASS,
            false, null, "reactor.core.publisher.Flux", true);

    /**
     * reactor Mono
     */
    public static final JavaCodeGenClassMeta REACTOR_MONO = new JavaCodeGenClassMeta("Mono", "Mono<T>", ClassType.CLASS,
            false, null, "reactor.core.publisher.Mono", true);


    /**
     * java数组类型标记
     */
    public static final JavaCodeGenClassMeta JAVA_ARRAY_CLASS_TYPE_MARK = new JavaCodeGenClassMeta(JavaArrayClassTypeMark.class, null, "", false);


    /**
     * or use 1L?
     */
    private final Long serialVersionUIDValue = SnowFlakeIdGenerator.SINGLETON.nextId();

    public JavaCodeGenClassMeta() {
    }

    public JavaCodeGenClassMeta(String name, String genericDescription, ClassType classType,
                                Boolean isAbstract, CommonCodeGenClassMeta superClass, String packagePath, Boolean needImport) {
        super(name, genericDescription, classType, isAbstract, superClass, packagePath, needImport, false);
    }

    public JavaCodeGenClassMeta(Class<?> source, String genericDescription, String packagePath, Boolean needImport) {
        this(source.getSimpleName(), genericDescription, getClassType(source), Modifier.isAbstract(source.getModifiers()), null, packagePath, needImport);
        this.source = source;
    }

    public static ClassType getClassType(Class<?> source) {
        if (source.isInterface()) {
            //接口
            return ClassType.INTERFACE;
        } else if (source.isEnum()) {
            //枚举
            return ClassType.ENUM;
        } else if (source.isAnnotation()) {
            return ClassType.ANNOTATION;
        } else {
            return ClassType.CLASS;
        }
    }


}
