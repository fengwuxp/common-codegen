package com.wuxp.codegen.list;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 列item
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class ColumnItem {

    /**
     * 列取值name
     */
    private String name;

    /**
     * 列标题
     */
    private String title;

    /**
     * 如过该列是枚举值则存在
     */
    private String[] valueEnum;


    /**
     * 渲染值的代码
     */
    private String renderCode;
}
