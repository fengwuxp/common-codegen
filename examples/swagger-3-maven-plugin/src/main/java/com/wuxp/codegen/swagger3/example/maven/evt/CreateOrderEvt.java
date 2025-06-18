package com.wuxp.codegen.swagger3.example.maven.evt;


import com.wuxp.codegen.swagger3.example.maven.enums.Sex;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.beans.Transient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

@Data
public class CreateOrderEvt extends BaseEvt {

    private static DateFormat dateFormat = new SimpleDateFormat();

    @Size(max = 50)
    private String sn;

    @NotNull
    private transient Integer totalAmount;

    private Map<Sex, String> test;

    @Transient
    public Integer getTotalAmount() {
        return totalAmount;
    }
}
