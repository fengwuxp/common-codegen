package com.wuxp.codegen.form;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表单描述对象
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class FormDescription {


    /**
     * 表单名称
     */
    private String name;

    /**
     * 提交url
     */
    private String submitUrl;

    /**
     * 一行的表单组件数量
     */
    private Integer rowNum;


    /**
     * 表单数组
     */
    private FormComponent[] formComponents;


}
