package com.wuxp.codegen.swagger3.example.evt;


import com.wuxp.codegen.swagger3.example.enums.Sex;
import java.beans.Transient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

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
