<#assign methodParamRequired=(method.params["req"].fieldMetas?size>0)>
<#-- 参数的所有字段是不是都非必填  -->
<#assign methodParamFileldAllNotRequired=false>
<#list method.params["req"].fieldMetas as paramField>
    <#if paramField.required>
        <#assign methodParamFileldAllNotRequired=false />
        <#break/>
    <#else >
        <#assign methodParamFileldAllNotRequired=true />
    </#if>
</#list>