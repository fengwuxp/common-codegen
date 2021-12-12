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

export const  ${method.name}=  (<#if method.isRequiredParameter(methodParamName)>req<#if method.isOptionalParameter(methodParamName)>?</#if>: ${method.getParameterTypeName(methodParamName)},</#if> options?: RequestOptionsInit): Promise<${customizeMethod.combineType(method.returnTypes)}> =>{
    <#assign tags=method.tags/>
    <#assign reqParamName="req"/>
    <#if (tags['needDeleteParams']?size>0)>
        <#assign reqParamName="reqData"/>
        const {<#list tags['needDeleteParams'] as deleteParamName>${deleteParamName},</#list>...reqData} = req;
    </#if>
    <#if tags['hasPathVariable']>
        const url =`${tags["url"]}`;
    </#if>
    <#if (tags['requestHeaderNames']?size>0)>
        <#--    设置请求头  -->
        const headers:Record<string,any>={};
        <#list tags['requestHeaderNames'] as requestHeaderName>
            if(${requestHeaderName}!=null){
              headers['${requestHeaderName}']=Array.isArray(${requestHeaderName})?${requestHeaderName}.join(";"):${requestHeaderName};
            }
        </#list>
    </#if>
  return request<${customizeMethod.combineType(method.returnTypes)}>(<#if tags['hasPathVariable']>url<#else >`${tags["url"]}`</#if>, {
      method: '${tags["httpMethod"]}',
    <#if (tags['requestHeaderNames']?size>0)>
        headers,
    </#if>
    <#if tags['supportBody']>
        <#if tags['requestType']??>
          requestType: '${tags['requestType']}',
        </#if>
        <#if method.isRequiredParameter(methodParamName)>
          data: ${reqParamName}<#if method.isOptionalParameter(methodParamName)> || {}</#if>,
         </#if>
    <#elseif method.isRequiredParameter(methodParamName)>
      params: ${reqParamName}<#if method.isOptionalParameter(methodParamName)> || {}</#if>,
    </#if>
    <#if tags['responseType']??>
      responseType: '${tags['responseType']}',
    </#if>
  ...(options || {} as RequestOptionsInit)
  })
  }

</#list>
