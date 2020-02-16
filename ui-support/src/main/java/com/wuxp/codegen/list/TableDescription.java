package com.wuxp.codegen.list;

import com.wuxp.codegen.form.FormDescription;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表格对象描述
 */
@Data
@Accessors(chain = true)
public class TableDescription {

    /**
     * 表格的列
     */
    private ColumnItem[] columnItems;


    /**
     * 查询表单
     */
    private FormDescription queryForm;


    /**
     * 加载表单代码
     */
    private String loadTableCode;

    /**
     * 查询表格代码
     */
    private String queryTableCode;
}
