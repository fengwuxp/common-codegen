<#list method.params?keys as key>
    <#assign methodParam=(method.params[key])>
    <#assign methodParamRequired=(methodParam.fieldMetas?size>0)>
</#list>
<#-- 参数的所有字段是不是都非必填  -->
<#assign methodParamFileldAllNotRequired=false>
<#list methodParam.fieldMetas as paramField>
    <#if paramField.required>
        <#assign methodParamFileldAllNotRequired=false />
        <#break/>
    <#else >
        <#assign methodParamFileldAllNotRequired=true />
    </#if>
</#list>