package com.wuxp.codegen.swagger3.example.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Data;

import java.util.Date;

@Data
@ApiResponse()
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Order extends BaseInfo<Long> {

    @Parameter()
    private String sn;

    private User user;


    public Date getAddTime() {
        return new Date();
    }
}
