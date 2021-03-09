package com.wuxp.codegen.model.extensions;

import com.wuxp.codegen.model.enums.ClassType;
import lombok.Data;

import java.util.List;


/**
 * 生成模型的描述对象
 *
 * @author wuxp
 */
@Data
public class SchemaCodegenModel {

    /**
     * 排序，数字越大越靠前
     */
    private int order;

    private String name;

    private Class<?> source;

    private String genericDescription;

    private String packagePath;

    private List<SchemaCodegenModelTypeVariable> typeVariables;

    private ClassType classType;

    private List<SchemaCodegenModelFieldMeta> fieldMetas;

    /**
     * 是否需要自动生成
     */
    protected Boolean needGenerate = true;
    /**
     * 是否需要导入的依赖
     */
    protected Boolean needImport = true;

}