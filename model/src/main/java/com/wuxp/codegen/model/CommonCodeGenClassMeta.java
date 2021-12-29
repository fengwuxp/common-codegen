package com.wuxp.codegen.model;

import com.wuxp.codegen.model.enums.ClassType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.beans.Transient;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用的代码生成 class 元数据
 *
 * @author wuxp
 */
@Data
@EqualsAndHashCode(exclude = {"superClass", "interfaces", "annotations", "dependencies"}, callSuper = true)
@Accessors(chain = true)
public class CommonCodeGenClassMeta extends CommonBaseMeta {

    /**
     * 数组类型的名称前缀
     */
    public static final String ARRAY_TYPE_NAME_PREFIX = "T[]";

    /**
     * 数组泛型描述符号 用于泛型合并
     */
    public static final String ARRAY_TYPE_GENERIC_DESCRIPTION = "[]<T>";

    /**
     * 数组
     */
    public static final CommonCodeGenClassMeta ARRAY = new CommonCodeGenClassMeta(ARRAY_TYPE_NAME_PREFIX, ARRAY_TYPE_GENERIC_DESCRIPTION,
            ClassType.INTERFACE, false, null, null, false, false);

    /**
     * type variable 类型变量
     */
    public static final CommonCodeGenClassMeta TYPE_VARIABLE = new CommonCodeGenClassMeta("T", "T", ClassType.CLASS, false, null, null, false,
            false);

    /**
     * 原目标类
     */
    protected Class<?> source;

    /**
     * 类型参数, 泛型
     */
    protected CommonCodeGenClassMeta[] typeVariables = new CommonCodeGenClassMeta[0];
    /**
     * 类类型
     */
    protected ClassType classType;
    /**
     * 是否为抽象的
     */
    protected Boolean isAbstract;
    /**
     * 超类
     */
    protected CommonCodeGenClassMeta superClass;
    /**
     * 实现的接口
     */
    protected CommonCodeGenClassMeta[] interfaces;
    /**
     * 注解
     */
    protected CommonCodeGenAnnotation[] annotations;
    /**
     * 包所在路径
     */
    protected String packagePath;
    /**
     * 依赖
     *
     * @key {@link CommonCodeGenClassMeta#getName}
     * @value {@link  CommonCodeGenClassMeta}
     */
    protected Map<String, ? extends CommonCodeGenClassMeta> dependencies = new LinkedHashMap<>();

    /**
     * 在有泛型时候的描述 例如 Set<T>,Map<K,V> 等
     */
    protected String genericDescription;

    /**
     * 方法列表
     */
    protected CommonCodeGenMethodMeta[] methodMetas = new CommonCodeGenMethodMeta[0];
    /**
     * 属性列表
     */
    protected CommonCodeGenFiledMeta[] fieldMetas = new CommonCodeGenFiledMeta[0];
    /**
     * 枚举常量列表
     *
     * @see #classType
     * @see ClassType#ENUM
     */
    protected CommonCodeGenFiledMeta[] enumConstants;
    /**
     * 是否需要自动生成
     */
    protected Boolean needGenerate = false;
    /**
     * 是否需要导入的依赖
     */
    protected Boolean needImport = true;
    /**
     * 属性类型 如果有泛型则有多个
     *
     * @key 类型，父类，接口，本身
     */
    private Map<String, ? extends CommonCodeGenClassMeta[]> superTypeVariables = Collections.emptyMap();

    /**
     * 是否为类型参数变量
     */
    private Boolean typeArgumentVariable = false;

    public CommonCodeGenClassMeta() {
    }


    public CommonCodeGenClassMeta(String name,
                                  String genericDescription,
                                  ClassType classType,
                                  Boolean isAbstract,
                                  CommonCodeGenClassMeta superClass,
                                  String packagePath,
                                  Boolean needImport,
                                  Boolean needGenerate) {
        this.name = name;
        this.genericDescription = genericDescription;
        this.classType = classType;
        this.isAbstract = isAbstract;
        this.superClass = superClass;
        this.packagePath = packagePath;
        this.needImport = needImport;
        this.needGenerate = needGenerate;
    }


    /**
     * @return 获取最终的泛型描述
     */
    public String getFinallyGenericDescription() {
        if (this.typeVariables == null || this.typeVariables.length == 0) {
            if (StringUtils.hasText(genericDescription)) {
                return genericDescription;
            }
            return getTypeIdent();
        }

        String typeDesc = Arrays.stream(this.typeVariables)
                .map(CommonCodeGenClassMeta::getFinallyGenericDescription)
                .collect(Collectors.joining(","));

        if (typeDesc.equals(getTypeIdent())) {
            // hack 泛型描述和类名相同
            return getTypeIdent();
        }
        return getTypeIdent() + "<" + typeDesc + ">";


    }

    /**
     * 获取最终的类名称，合并了泛型描述 形如：A<String,Long> or A<K,V>
     */
    public String getFinallyClassName() {
        return getFinallyGenericDescription();
    }

    public String getTypeIdent() {
        return this.name;
    }

    @Transient
    public boolean isNeedImport() {
        return Boolean.TRUE.equals(this.needImport);
    }
}
