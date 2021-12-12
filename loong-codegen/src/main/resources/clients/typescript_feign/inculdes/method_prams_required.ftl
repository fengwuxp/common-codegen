<#--获取参数名称，-->
<#assign methodParamName="req">
<#list method.params?keys as key>
    <#assign methodParamName=key/>
</#list>