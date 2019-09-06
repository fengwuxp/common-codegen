package com.wuxp.codegen.swagger3.example.evt;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateOrderEvt extends BaseEvt {


    @Size(max = 50)
    private String sn;

    @NotNull
    private Integer totalAmount;

}
