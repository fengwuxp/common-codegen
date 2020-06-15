package com.wuxp.codegen.swagger3.example.evt;


import com.wuxp.api.context.InjectField;

import javax.validation.constraints.Size;

public class QueryOrderEvt extends BaseQueryEvt {

    @Size(max = 50)
    @InjectField(value = "#sn")
    private String sn;

    private int[] ids;

}
