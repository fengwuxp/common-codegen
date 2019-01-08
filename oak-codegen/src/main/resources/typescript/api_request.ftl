<#import "../common/customize_method.ftl" as customize_method/>

<#--依赖导入处理-->
<#list dependencies as key,val >
    import {${key}} from "@/src/${val.packagePath}";
</#list>

/**
<#list comments as cmment>
 * ${cmment}
</#list>
 **/

<#list annotations as annotation>
    @${annotation.name}({
    <#list annotation.namedArguments as name,val>
        ${name}:${val},
    </#list>
    })
</#list>
export interface  ${finallyClassName}<#if superClass??> extends ${superClass.finallyClassName}</#if> {

<#list filedMetas as field>
    /**
    <#list field.comments as cmment>
     *${cmment}
    </#list>
     **/
    ${field.name}<#if !field.required>?</#if>: ${customize_method.combineType(field.filedTypes)};
</#list>
}