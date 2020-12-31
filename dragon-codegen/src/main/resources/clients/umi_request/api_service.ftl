/* tslint:disable */
import request,{RequestOptionsInit} from 'umi-request';
import queryString from "querystring";
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
/*===============分割线=======================*/
</#if>


<#list methodMetas as method>
 <#if (method.comments?size>0)>
/**
<#list method.comments as cmment>
* ${cmment_index+1}:${cmment}
</#list>
**/
</#if>
export const  ${method.name}=  (req: ${method.params["req"].name}, options?: RequestOptionsInit): Promise<${customize_method.combineType(method.returnTypes)}> =>{
  <#assign tags=method.tags/>
   return request<${customize_method.combineType(method.returnTypes)}>(`${tags["url"]}`, {
          ...(options||{}),
          method: '${tags["httpMethod"]}',
    <#if tags['supportBody']>
        headers:{
           'Content-Type':'${tags["mediaType"]}'
        },
<#--       <#if tags['useForm']>-->
<#--          data:queryString(req),-->
<#--          <#else >-->
<#--          data:JSON.stringify(req),-->
<#--       </#if>-->
        data:req
    <#else >
        params: queryString(req)
    </#if>

   })
}

</#list>
