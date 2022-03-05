package com.wuxp.codegen.swagger3.example.evt;


import com.wuxp.codegen.swagger3.example.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.Transient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreateOrderEvt extends BaseEvt {

    private static DateFormat dateFormat = new SimpleDateFormat();

    @Size(max = 50)
    @Schema(required = true)
    private String sn;

    @NotNull
    private transient int totalAmount;

    private Map<Sex, String> test;

    @Transient
    public Integer getTotalAmount() {
        return totalAmount;
    }
}
