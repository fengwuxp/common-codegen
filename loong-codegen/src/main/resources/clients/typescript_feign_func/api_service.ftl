/* tslint:disable */
/* eslint-disable */
<#include "../typescript_feign/inculdes/feign_imports.ftl">
<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        import {${key}} from "${customizeMethod.pathResolve(packagePath,val.packagePath)}";
    </#list>
</#if>

<#include "../../commons/api_client_comments.ftl">
<#list annotations as annotation>
    <#if annotation.name =='Feign'>
        const API_FUNCTION_FACTORY = feignHttpFunctionBuilder({
        <#list annotation.namedArguments as name,val>
            ${name}:${val},
        </#list>
        });
    </#if>
</#list>
<#list methodMetas as method>
    <#include "../../commons/api_method_comments.ftl">
    <#include "../typescript_feign/inculdes/method_prams_required.ftl">
    <#assign httpMethod=''/>
    <#list method.annotations as annotation>
        <#if annotation.name=='GetMapping'>
            <#assign httpMethod='get'/>
        <#elseif annotation.name=='PostMapping'>
            <#assign httpMethod='post'/>
        <#elseif annotation.name=='PutMapping'>
            <#assign httpMethod='put'/>
        <#elseif annotation.name=='PatchMapping'>
            <#assign httpMethod='patch'/>
        <#elseif annotation.name=='DeleteMapping'>
            <#assign httpMethod='delete'/>
        <#elseif annotation.name=='RequestMapping'>
            <#assign httpMethod=annotation.namedArguments['value'].toLowerCase()/>
        </#if>
    </#list>
    <#assign methodName=method.name/>
    <#if methodName=='delete'>
        <#assign methodName='delete_request'/>
    </#if>
    export const ${methodName}: FeignHttpClientPromiseFunction<<#if method.isRequiredParameter(methodParamName)>${method.getParameterTypeName(methodParamName)} <#if method.isOptionalParameter(methodParamName)>|void</#if><#else >void</#if>,${customizeMethod.combineType(method.returnTypes)}> = API_FUNCTION_FACTORY.${httpMethod}({
    <#list method.annotations as annotation>
        <#if annotation.name?ends_with('Mapping')>
            <#list annotation.namedArguments as name,val>
                ${name}:${val},
            </#list>
        </#if>
    </#list>
    });
</#list>
