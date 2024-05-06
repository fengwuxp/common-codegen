/* tslint:disable */
/* eslint-disable */

<#include "./inculdes/feign_imports.ftl">
<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
      import {${key}} from "${customizeMethod.pathResolve(packagePath,val.packagePath)}";
    </#list>
</#if>

<#include "../../commons/api_client_comments.ftl">
<#list annotations as annotation>
  @${annotation.name}({
    <#list annotation.namedArguments as name,val>
        ${name}:${val},
    </#list>
  })
</#list>
class ${name}{

<#list methodMetas as method>
    <#include "../../commons/api_method_comments.ftl">
    <#list method.annotations as annotation>
      @${annotation.name}({
        <#list annotation.namedArguments as name,val>
            ${name}:${val},
        </#list>
      })
    </#list>
    <#include "./inculdes/method_prams_required.ftl">
    ${method.name}!:(<#if method.isRequiredParameter(methodParamName)>req<#if method.isOptionalParameter(methodParamName)>?</#if>: ${method.getParameterTypeName(methodParamName)}<#else >req?: null | undefined</#if>, option?: FeignRequestOptions) => Promise<${customizeMethod.combineType(method.returnTypes)}>;
</#list>
}

export default new ${name}();
