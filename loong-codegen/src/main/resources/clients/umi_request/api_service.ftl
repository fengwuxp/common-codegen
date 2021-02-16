/* tslint:disable */
<#if umiModel=='OPEN_SOURCE'>
  import request,{RequestOptionsInit} from 'umi-request';
<#else >
  import {request} from '@alipay/bigfish';
  import {RequestOptionsInit} from 'umi-request';
</#if>
<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
      import {${key}} from "${customizeMethod.pathResolve(packagePath,val.packagePath)}";
    </#list>
</#if>

<#include "../../commons/api_client_comments.ftl">
<#if (comments?size>0)>
/*================================================分割线，以下为接口列表===================================================*/
</#if>

<#list methodMetas as method>
    <#include "../../commons/api_method_comments.ftl">
    <#include "../typescript_feign/inculdes/method_prams_required.ftl">

  export const  ${method.name}=  (<#if methodParamRequired>req<#if methodParamFileldAllNotRequired>?</#if>: ${method.params["req"].name},</#if> options?: RequestOptionsInit): Promise<${customizeMethod.combineType(method.returnTypes)}> =>{
    <#assign tags=method.tags/>
  return request<${customizeMethod.combineType(method.returnTypes)}>(`${tags["url"]}`, {
  method: '${tags["httpMethod"]}',
    <#if tags['supportBody']>
        <#if tags['requestType']??>
          requestType: '${tags['requestType']}',
        </#if>
        <#if methodParamRequired>
          data: req<#if methodParamFileldAllNotRequired> || {}</#if>,
         </#if>
    <#elseif methodParamRequired>
      params: req<#if methodParamFileldAllNotRequired> || {}</#if>,
    </#if>
    <#if tags['responseType']??>
      responseType: '${tags['responseType']}',
    </#if>
  ...(options || {} as RequestOptionsInit)
  })
  }

</#list>