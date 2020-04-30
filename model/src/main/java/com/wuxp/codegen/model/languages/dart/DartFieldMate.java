package com.wuxp.codegen.model.languages.dart;

import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * dart的field 元数据
 * @author wxup
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public final class DartFieldMate extends CommonCodeGenFiledMeta {

    /**
     * 是否必填
     */
    private Boolean required = false;

}
