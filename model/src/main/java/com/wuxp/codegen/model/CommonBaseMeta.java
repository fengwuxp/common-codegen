package com.wuxp.codegen.model;

import com.wuxp.codegen.model.enums.AccessPermission;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;


/**
 * 通用的基础元数据类
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class CommonBaseMeta implements Comparable<CommonBaseMeta> {


  /**
   * 如果是类则为类的simple名(不含包名) 如果为属性或方法则为name
   */
  @NonNull
  protected String name;

  //是否为静态
  protected Boolean isStatic = false;

  //是否为final
  protected Boolean isFinal = false;

  //访问权限控制
  protected AccessPermission accessPermission = AccessPermission.PUBLIC;

  /**
   * 注释列表
   */
  protected String[] comments = new String[]{};

  /**
   * 其他的描述信息
   */
  protected Map<String, Object> tags = new HashMap<>();


  public CommonBaseMeta() {

  }

  public CommonBaseMeta(String name) {
    this.name = name;
  }

  public String getAccessPermissionName() {

    return this.accessPermission.getValue();
  }


  public <T> T getTag(String key) {
    Map<String, Object> tags = this.tags;
    Object o = tags.get(key);
    if (o == null) {
      return null;
    }
    return (T) o;
  }


  @Override
  public int compareTo(CommonBaseMeta o) {
    return this.hashCode() - o.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CommonBaseMeta that = (CommonBaseMeta) o;

    if (!Objects.equals(name, that.name)) {
      return false;
    }
    if (!Objects.equals(isStatic, that.isStatic)) {
      return false;
    }
    if (!Objects.equals(isFinal, that.isFinal)) {
      return false;
    }
    if (accessPermission != that.accessPermission) {
      return false;
    }
    // Probably incorrect - comparing Object[] arrays with Arrays.equals
    if (!Arrays.equals(comments, that.comments)) {
      return false;
    }
    return Objects.equals(tags, that.tags);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (isStatic != null ? isStatic.hashCode() : 0);
    result = 31 * result + (isFinal != null ? isFinal.hashCode() : 0);
    result = 31 * result + (accessPermission != null ? accessPermission.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(comments);
    result = 31 * result + (tags != null ? tags.hashCode() : 0);
    return result;
  }
}
