package ${packagePath?replace('.'+name,'')};
<#if comments??>
    /**
    <#list comments as cmment>
        * ${cmment}
    </#list>
    **/
</#if>
public enum  ${name}{


<#assign len=filedMetas?size/>
<#list filedMetas as field>
    ${field.name}("${field.comments[0]}")<#if  field_has_next>,<#else>;</#if>
</#list>


private String desc;

${name}(String desc) {
this.desc = desc;
}


public String getDesc() {
return desc;
}

}