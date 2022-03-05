package com.wuxp.codegen.model.languages.dart;

import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * dart的 field 元数据
 *
 * @author wxup
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public final class DartFieldMate extends CommonCodeGenFiledMeta {

}
