/* tslint:disable */

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
      import {${key}} from "${customizeMethod.pathResolve(packagePath,val.packagePath)}";
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
            ${name}:${val},
        </#list>
      })
    </#list>
</#if>
export interface  ${finallyClassName}<#if superClass??> extends ${superClass.finallyClassName}</#if> {

<#if fieldMetas??>
    <#list fieldMetas as field>
        <#if (field.comments?size>0)>
          /**
            <#list field.comments as cmment>
              *${cmment}
            </#list>
            **/
        </#if>
        ${field.name}<#if field.required?string('true', 'false')=='false'>?</#if>: ${customizeMethod.combineType(field.filedTypes)};
<#--        <#assign filedType=customizeMethod.combineType(field.filedTypes)/>-->
<#--        <#if filedType?starts_with('Enum_Key_Record<')>-->
<#--            ${field.name}<#if !field.required!false>?</#if>: ${filedType?replace('Enum_Key_Record<','Omit<Partial<Record<keyof typeof ')+'>, "prototype">'};-->
<#--        <#else >-->
<#--            ${field.name}<#if !field.required!false>?</#if>: ${filedType};-->
<#--        </#if>-->

    </#list>
</#if>
}
