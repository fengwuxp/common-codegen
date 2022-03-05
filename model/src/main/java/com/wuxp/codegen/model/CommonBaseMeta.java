package com.wuxp.codegen.model;

import com.wuxp.codegen.model.enums.AccessPermission;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.TreeMap;


/**
 * 通用的基础元数据类
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class CommonBaseMeta implements Comparable<CommonBaseMeta> {

    /**
     * 如果是类则为类的 simple 名(不含包名) 如果为属性或方法则为name
     */
    protected String name;

    /**
     * 是否为静态
     */
    protected Boolean isStatic = false;

    /**
     * 是否为final
     */
    protected Boolean isFinal = false;

    /**
     * 访问权限控制
     */
    protected AccessPermission accessPermission = AccessPermission.PUBLIC;

    /**
     * 注释列表
     */
    protected String[] comments = new String[]{};

    /**
     * 其他的描述信息
     */
    protected Map<String, Object> tags = new TreeMap<>();


    public CommonBaseMeta() {

    }

    public CommonBaseMeta(String name) {
        this.name = name;
    }

    public String getAccessPermissionName() {

        return this.accessPermission.getValue();
    }

    public <T> T getTag(String key) {
        if (this.tags == null) {
            return null;
        }
        Object o = this.tags.get(key);
        if (o == null) {
            return null;
        }
        return (T) o;
    }


    @Override
    public int compareTo(CommonBaseMeta o) {
        if (this.name == null || o.getName() == null) {
            return this.tags.size() - o.getTags().size();
        }
        return this.getName().compareTo(o.getName());
    }

}
