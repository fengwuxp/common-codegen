
<#if comments??>
    /**
    <#list comments as cmment>
        * ${cmment}
    </#list>
    **/
</#if>
public enum  ${name}{

private String desc;

constructor() {}

<#list filedMetas as field>

    ${field.name}("${field.comments[0]}"),
</#list>


}