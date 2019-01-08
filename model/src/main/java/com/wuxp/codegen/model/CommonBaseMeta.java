package com.wuxp.codegen.model;

import com.wuxp.codegen.model.enums.AccessPermission;
import lombok.Data;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;


/**
 * 通用的基础元数据类
 */
@Data
public class CommonBaseMeta implements Comparable<CommonBaseMeta> {

    /**
     * 如果是类则为类的simple名(不含包名)
     * 如果为属性或方法则为name
     */
    protected String name;

    //是否为静态
    protected Boolean isStatic;

    //是否为final
    protected Boolean isFinal;

    //访问权限控制
    protected AccessPermission accessPermission;

    /**
     * 注释列表
     */
    protected String[] comments;

    /**
     * 其他的描述信息
     */
    protected Map<String, Object> tags = new HashMap<>();

    /**
     * 类型参数, 泛型
     */
    protected Type[] typeVariables;


    public CommonBaseMeta() {

    }

    public CommonBaseMeta(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(CommonBaseMeta o) {
        return this.hashCode() - o.hashCode();
    }
}
