/* tslint:disable */

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

        <#assign returnType=customize_method.combineType(field.filedTypes)/>
        <#if returnType?starts_with('Enum_Key_Record<')>
          ${field.name}<#if !field.required!false>?</#if>: ${returnType?replace('Enum_Key_Record<','Omit<Partial<Record<keyof typeof ')+'>, "prototype">'};
        <#else >
         ${field.name}<#if !field.required!false>?</#if>: ${returnType};
        </#if>

    </#list>
</#if>
}
