package com.wuxp.codegen.swagger3.example.evt;


import com.wuxp.api.context.InjectField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author wuxp
 */
@Data
public class QueryOrderEvt extends BaseQueryEvt {

    @Size(max = 50)
    @InjectField(value = "#sn")
    @Schema(hidden = true)
    private String sn;

    private int[] ids;

}
