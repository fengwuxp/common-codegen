package com.wuxp.codegen.swagger3.example.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Order extends BaseInfo<Long> {

    @Parameter()
    private String sn;

    private User user;


    public Date getAddTime() {
        return new Date();
    }
}
