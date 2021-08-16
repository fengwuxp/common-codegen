package com.wuxp.codegen.model.languages.typescript;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.enums.ClassType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 主要用定义 typescript 原本支持的类
 *
 * @author wuxp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public final class TypescriptClassMeta extends CommonCodeGenClassMeta {

    /**
     * object
     */
    public static final TypescriptClassMeta OBJECT = new TypescriptClassMeta("object", null, ClassType.CLASS, false, null);

    public static final TypescriptClassMeta ANY = new TypescriptClassMeta("any", "any", ClassType.CLASS, false, null);

    /**
     * string
     */
    public static final TypescriptClassMeta STRING = new TypescriptClassMeta("string", null, ClassType.CLASS, false, OBJECT);

    /**
     * number
     */
    public static final TypescriptClassMeta NUMBER = new TypescriptClassMeta("number", null, ClassType.CLASS, false, OBJECT);

    public static final TypescriptClassMeta BIGINT = new TypescriptClassMeta("BigInt", null, ClassType.CLASS, false, OBJECT);

    /**
     * boolean
     */
    public static final TypescriptClassMeta BOOLEAN = new TypescriptClassMeta("boolean", null, ClassType.CLASS, false, OBJECT);

    /**
     * array
     */
    public static final TypescriptClassMeta ARRAY = new TypescriptClassMeta("Array", "Array<T>", ClassType.CLASS, false, OBJECT);

    /**
     * map
     */
    public static final TypescriptClassMeta MAP = new TypescriptClassMeta("Map", "Map<K,V>", ClassType.CLASS, false, OBJECT);

    /**
     * set
     */
    public static final TypescriptClassMeta SET = new TypescriptClassMeta("Set", "Set<V>", ClassType.CLASS, false, OBJECT);

    /**
     * promise
     */
    public static final TypescriptClassMeta PROMISE = new TypescriptClassMeta("Promise", "Promise<V>", ClassType.CLASS, false, OBJECT);

    /**
     * regExp
     */
    public static final TypescriptClassMeta REGEXP = new TypescriptClassMeta("RegExp", null, ClassType.CLASS, false, OBJECT);

    /**
     * Date
     */
    public static final TypescriptClassMeta DATE = new TypescriptClassMeta("Date", null, ClassType.CLASS, false, OBJECT);


    /**
     * void
     */
    public static final TypescriptClassMeta VOID = new TypescriptClassMeta("void", null, ClassType.CLASS, false, null);

    /**
     * record
     */
    public static final TypescriptClassMeta RECORD = new TypescriptClassMeta("Record", "Record<K,V>", ClassType.CLASS, false, OBJECT);

    /**
     * 枚举key的 record
     */
    public static final TypescriptClassMeta ENUM_KEY_RECORD = new TypescriptClassMeta("Enum_Key_Record", "Enum_Key_Record<K,V>",
            ClassType.CLASS, false, OBJECT);

    /**
     * 枚举类型
     */
    public static final TypescriptClassMeta ENUM = new TypescriptClassMeta("Enum", null, ClassType.INTERFACE, false, null, "Enum");


    /**
     * web file
     */
    public static final TypescriptClassMeta BROWSER_FILE = new TypescriptClassMeta("File", null, ClassType.CLASS, false, OBJECT);

    /**
     * java数组类型标记
     */
    public static final TypescriptClassMeta JAVA_ARRAY_CLASS_TYPE_MARK = new TypescriptClassMeta("JavaArrayClassTypeMark", null,
            ClassType.CLASS, false, OBJECT);

    static {
        ENUM.setFieldMetas(new CommonCodeGenFiledMeta[]{
                new TypescriptFieldMate("name", new TypescriptClassMeta[]{STRING}, true),
                new TypescriptFieldMate("ordinal", new TypescriptClassMeta[]{NUMBER}, true),
                new TypescriptFieldMate("desc", new TypescriptClassMeta[]{STRING}, true),
        });
        ENUM.setNeedImport(true);
        ENUM.setNeedGenerate(true);

    }

    /**
     * 枚举的名称列表
     */
    private String enumNames;

    public TypescriptClassMeta() {
    }

    public TypescriptClassMeta(String name,
                               String genericDescription,
                               ClassType classType,
                               Boolean isAbstract,
                               TypescriptClassMeta superClass,
                               String packagePath) {
        this(name, genericDescription, classType, isAbstract, superClass);
        this.packagePath = packagePath;
        this.needImport = true;
    }

    private TypescriptClassMeta(String name,
                                String genericDescription,
                                ClassType classType,
                                Boolean isAbstract,
                                TypescriptClassMeta superClass) {
        this.name = name;
        this.genericDescription = genericDescription;
        this.classType = classType;
        this.isAbstract = isAbstract;
        this.superClass = superClass;
        this.needGenerate = false;
        this.needImport = false;
    }


    @Override
    public String getTypeIdent() {
        return enumNames == null ? super.getTypeIdent() : enumNames;
    }
}
