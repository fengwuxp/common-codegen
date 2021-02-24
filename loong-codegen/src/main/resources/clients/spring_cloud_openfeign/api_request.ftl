package ${packagePath?replace('.'+name,'')};

import lombok.Data;

import javax.validation.constraints.NotNull;

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
      import ${customizeMethod.pathResolve(packagePath,val.packagePath)};
    </#list>
</#if>

<#if (comments?size>0)>
  /**
    <#list comments as cmment>
      * ${cmment}
    </#list>
  **/
</#if>

<#if annotations??>
    <#list annotations as annotation>
      @${annotation.name}({
        <#list annotation.namedArguments as name,val>
            ${name}=${val}
        </#list>
      })
    </#list>
</#if>

@Data
public class  ${finallyClassName}<#if superClass??> extends ${superClass.finallyClassName}</#if> {

<#if fieldMetas??>
    <#list fieldMetas as field>
        <#if (field.comments?size>0)>
          /**
            <#list field.comments as cmment>
              *${cmment}
            </#list>
            **/
        </#if>
        ${field.accessPermissionName} ${customizeMethod.combineType(field.filedTypes)} ${field.name};
    </#list>
</#if>
}
