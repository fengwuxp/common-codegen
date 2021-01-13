/* tslint:disable */
<#include "./inculdes/feign_imports.ftl">
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

<#list annotations as annotation>
  @${annotation.name}({
    <#list annotation.namedArguments as name,val>
        ${name}:${val},
    </#list>
  })
</#list>
class ${name}{

<#list methodMetas as method>
    <#if (method.comments?size>0)>
      /**
        <#list method.comments as cmment>
          * ${cmment_index+1}:${cmment}
        </#list>
      **/
    </#if>
    <#list method.annotations as annotation>
      @${annotation.name}({
        <#list annotation.namedArguments as name,val>
            ${name}:${val},
        <#--            ${name}: ${val?replace('MediaType.','HttpMediaType.')},-->
        </#list>
      })
    </#list>
    <#include "./inculdes/method_prams_required.ftl">
    ${method.name}!:(<#if methodParamRequired>req<#if methodParamFileldAllNotRequired>?</#if>: ${method.params["req"].name}<#else >req?: null | undefined</#if>, option?: FeignRequestOptions) => Promise<${customizeMethod.combineType(method.returnTypes)}>;
</#list>
}

export default new ${name}();
