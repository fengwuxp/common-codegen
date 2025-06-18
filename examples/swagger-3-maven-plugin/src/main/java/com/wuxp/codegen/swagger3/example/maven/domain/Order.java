package com.wuxp.codegen.swagger3.example.maven.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Order extends BaseInfo<Long, String> {

    @Parameter()
    private String sn;

    private User user;

    @Schema(description = "添加时间")
    public Date getAddTime() {
        return new Date();
    }
}
