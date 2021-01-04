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
        import {${key}} from "${customize_method.pathoResolve(packagePath,val.packagePath)}";
    </#list>
</#if>

<#if (comments?size>0)>
    /**
    <#list comments as cmment>
    * ${cmment}
    </#list>
    **/
/*================================================分割线，以下为接口列表===================================================*/
</#if>


<#list methodMetas as method>
<#if (method.comments?size>0)>
/**
<#list method.comments as cmment>
* ${cmment_index+1}:${cmment}
</#list>
**/
</#if>
<#include "../typescript_feign/inculdes/method_prams_required.ftl">

export const  ${method.name}=  (<#if methodParamRequired>req<#if methodParamFileldAllNotRequired>?</#if>: ${method.params["req"].name},</#if> options?: RequestOptionsInit): Promise<${customize_method.combineType(method.returnTypes)}> =>{
  <#assign tags=method.tags/>
   return request<${customize_method.combineType(method.returnTypes)}>(`${tags["url"]}`, {
      method: '${tags["httpMethod"]}',
    <#if tags['supportBody'] && methodParamRequired>
      <#if tags['useForm']>
      requestType: 'form',
      data: req<#if methodParamFileldAllNotRequired> || {}</#if>,
      <#else >
      requestType: 'json',
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
