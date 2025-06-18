package com.wuxp.codegen.swagger3.example.maven.evt;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author wuxp
 */
@Data
public class QueryOrderEvt extends BaseQueryEvt {

    @Size(max = 50)
    @Schema(hidden = true)
    private String sn;

    private int[] ids;

    @Schema(description = "用户id", hidden = true)
    private Long memberId;

}
