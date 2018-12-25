package com.wuxp.codegen.model.languages.typescript;

import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import lombok.Data;

/**
 * typescript的field 元数据
 */
@Data
public final class TypescriptFieldMate extends CommonCodeGenFiledMeta {

    /**
     * 是否必须
     */
    private Boolean required;

}
