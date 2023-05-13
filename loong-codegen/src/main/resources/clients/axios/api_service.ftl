/* tslint:disable */
import axios, {AxiosRequestConfig,AxiosResponse} from 'axios';
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

export const  ${method.name}=  (<#if method.isRequiredParameter(methodParamName)>req<#if method.isOptionalParameter(methodParamName)>?</#if>: ${method.getParameterTypeName(methodParamName)},</#if> options?: AxiosRequestConfig): Promise<AxiosResponse<${customizeMethod.combineType(method.returnTypes)}>> =>{
    <#assign tags=method.tags/>
    <#assign reqParamName="req"/>
    <#if (tags['needDeleteParams']?size>0)>
        <#assign reqParamName="reqData"/>
        const {<#list tags['needDeleteParams'] as deleteParamName>${deleteParamName},</#list>...reqData} = req;
    </#if>
    <#if tags['hasPathVariable']>
        const url =`${tags["url"]}`;
    </#if>
    <#if (tags['requestHeaderNames']?size>0 || tags['requestContentType']??)>
        <#--    设置请求头  -->
        const headers:Record<string,any>={};
        <#list tags['requestHeaderNames'] as requestHeaderName>
            if(${requestHeaderName}!=null){
              headers['${requestHeaderName}']=Array.isArray(${requestHeaderName})?${requestHeaderName}.join(";"):${requestHeaderName};
            }
        </#list>
        <#if tags['requestContentType']??>
            <#if tags['requestContentType']??>
                headers['Content-Type']= '${tags['requestContentType']}';
            </#if>
        </#if>
    </#if>
  return axios.request<${customizeMethod.combineType(method.returnTypes)}>( {
      url:<#if tags['hasPathVariable']>url<#else >`${tags["url"]}`</#if>,
      method: '${tags["httpMethod"]}',
    <#if (tags['requestHeaderNames']?size>0 || tags['requestContentType']??)>
        headers,
    </#if>
    <#if tags['supportBody']>
        <#if method.isRequiredParameter(methodParamName)>
          data: ${reqParamName}<#if method.isOptionalParameter(methodParamName)> || {}</#if>,
         </#if>
    <#elseif method.isRequiredParameter(methodParamName)>
      params: ${reqParamName}<#if method.isOptionalParameter(methodParamName)> || {}</#if>,
    </#if>
    <#if tags['responseType']??>
      responseType: '${tags['responseType']}',
    </#if>
  ...(options || {} as AxiosRequestConfig)
  })
  }

</#list>
