package com.wuxp.codegen.model.languages.dart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * dart built value factory model
 *
 * @author wxup
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DartBuiltValueFactoryModel implements Serializable, Comparable<DartBuiltValueFactoryModel> {

    private static final long serialVersionUID = -3359935455859198968L;

    /**
     * fullType code
     */
    private String fullTypeCode;


    /**
     * function code
     */
    private String functionCode;

    @Override
    public int compareTo(DartBuiltValueFactoryModel o) {
        return this.fullTypeCode.compareTo(o.fullTypeCode);
    }
}
