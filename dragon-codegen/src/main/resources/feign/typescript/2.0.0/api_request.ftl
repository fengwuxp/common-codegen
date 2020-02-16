<#--<#import "../common/customize_method.ftl" as customize_method/>-->

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        import {${key}} from "${customize_method.pathoResolve(packagePath,val.packagePath)}";
    </#list>
</#if>

<#if comments??>
    /**
    <#list comments as cmment>
    * ${cmment}
    </#list>
    **/
</#if>

<#if fieldMetas??>
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
        /**
        <#list field.comments as cmment>
            *${cmment}
        </#list>
        **/
<#--        ${field.name}<#if field.required?string('true', 'false')=='false'>?</#if>: ${customize_method.combineType(field.filedTypes)};-->
        ${field.name}<#if !field.required>?</#if>: ${customize_method.combineType(field.filedTypes)};
    </#list>
</#if>
}
