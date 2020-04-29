package com.wuxp.codegen.model.languages.typescript;

import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * typescript的field 元数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public final class TypescriptFieldMate extends CommonCodeGenFiledMeta {

    /**
     * 是否必填
     */
    private Boolean required = false;

}
