package com.wuxp.codegen.swagger3.example.evt;


import javax.validation.constraints.Size;

public class QueryOrderEvt extends BaseQueryEvt {

    @Size(max = 50)
    private String sn;

    private int[] ids;

}
