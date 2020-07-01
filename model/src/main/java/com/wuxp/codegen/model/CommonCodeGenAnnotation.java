package com.wuxp.codegen.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 通用的代码生成注解描述元数据
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class CommonCodeGenAnnotation extends CommonBaseMeta {


    /**
     * 命名参数
     */
    private Map<String, String> namedArguments;

    /**
     * 位置参数
     */
    private List<String> positionArguments;

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
        CommonCodeGenAnnotation that = (CommonCodeGenAnnotation) o;
        return Objects.equals(namedArguments, that.namedArguments) &&
                Objects.equals(positionArguments, that.positionArguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), namedArguments, positionArguments);
    }
}
