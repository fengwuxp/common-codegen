package com.wuxp.codegen.swagger3.example.maven.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Order extends BaseInfo<Long, String> {

    @Parameter()
    private String sn;

    private User user;

    @Schema(description = "添加时间")
    public Date getAddTime() {
        return new Date();
    }
}
