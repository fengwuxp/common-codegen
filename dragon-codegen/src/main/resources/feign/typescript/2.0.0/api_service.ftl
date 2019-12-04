<#include "./inculdes/feign_imports.ftl">

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
    <#--import {${key}} from "@api${val.packagePath}";-->
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

<#list annotations as annotation>
    @${annotation.name}({
    <#list annotation.namedArguments as name,val>
        ${name}:${val},
    </#list>
    })
</#list>
class ${name}{

<#list methodMetas as method>
    /**
    <#list method.comments as cmment>
        * ${cmment_index+1}:${cmment}
    </#list>
    **/
    <#list method.annotations as annotation>
        @${annotation.name}({
        <#list annotation.namedArguments as name,val>
            ${name}:${val},
<#--            ${name}: ${val?replace('MediaType.','HttpMediaType.')},-->
        </#list>
        })
    </#list>
    ${method.name}:(req: ${method.params["req"].name}, option?: FeignRequestOptions) => ${customize_method.combineType(method.returnTypes)};
</#list>
}

export default new ${name}();
