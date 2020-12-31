package ${packagePath?replace('.'+name,'')};
<#if (comments?size>0)>
    /**
    <#list comments as cmment>
    * ${cmment}
    </#list>
    **/
</#if>
public enum  ${name}{


<#assign len=fieldMetas?size/>
<#list fieldMetas as field>
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
