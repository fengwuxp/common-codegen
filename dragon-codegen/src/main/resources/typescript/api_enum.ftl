import {Enum} from "oak_common/src/enums/Enum";

<#if comments??>
    /**
    <#list comments as cmment>
        * ${cmment}
    </#list>
    **/
</#if>
export class ${name}{

constructor() {}

<#list filedMetas as field>
    <#assign commentLength = field.comments?size />
    public static readonly ${field.name}:Enum={
    name:"${field.name}",
    ordinal:${field_index},
    desc: <#if commentLength??>"${field.comments[0]}"<#else>"${field.name}"</#if>
    };
</#list>


}