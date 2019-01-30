package com.wuxp.codegen.dragon.model.languages.typescript;

import com.wuxp.codegen.dragon.model.CommonCodeGenFiledMeta;
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
     * 是否必须
     */
    private Boolean required;

}
