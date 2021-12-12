package com.wuxp.codegen.model.languages.typescript;

import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * typescript的field 元数据
 *
 * @author wuxp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public final class TypescriptFieldMate extends CommonCodeGenFiledMeta {


    public TypescriptFieldMate() {
    }

    public TypescriptFieldMate(String name, TypescriptClassMeta[] filedTypes, boolean required) {
        this.name = name;
        this.filedTypes = filedTypes;
        this.setRequired(required);
    }
}
