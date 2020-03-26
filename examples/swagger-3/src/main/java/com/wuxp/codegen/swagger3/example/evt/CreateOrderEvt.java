package com.wuxp.codegen.swagger3.example.evt;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Data
public class CreateOrderEvt extends BaseEvt {

    private static DateFormat dateFormat=new SimpleDateFormat();

    @Size(max = 50)
    private String sn;

    @NotNull
    private Integer totalAmount;

}
