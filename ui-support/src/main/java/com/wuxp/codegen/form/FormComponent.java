package com.wuxp.codegen.form;


import com.wuxp.codegen.enums.FormComponentType;
import com.wuxp.codegen.validation.ValidatorRuleItem;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表单组件
 */
@Data
@Accessors(chain = true)
public class FormComponent {

    /**
     * component type
     */
    private FormComponentType type;

    /**
     * 表单组件提交的名称名称
     */
    private String name;

    /**
     * \
     * 表单label
     */
    private String label;


    /**
     * 默认值
     */
    private String defaultValue;


    /**
     * 验证规则
     */
    private ValidatorRuleItem ruleItem;


}
