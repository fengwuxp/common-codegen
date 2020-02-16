package com.wuxp.codegen.validation;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 验证规则item
 */
@Data
@Accessors(chain = true)
public class ValidatorRuleItem {


    private String type;
}
