package com.wuxp.codegen.model.languages.dart;

import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * dart的field 元数据
 * @author wxup
 */
@Data
@Accessors(chain = true)
public final class DartFieldMate extends CommonCodeGenFiledMeta {

    /**
     * 是否必填
     */
    private Boolean required = false;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DartFieldMate that = (DartFieldMate) o;
        return Objects.equals(required, that.required);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), required);
    }
}
