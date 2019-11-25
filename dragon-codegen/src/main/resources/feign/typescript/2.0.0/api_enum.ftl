import {Enum} from "fengwuxp-typescript-feign";

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
    public static readonly ${field.name}:Enum={
    name:"${field.name}",
    ordinal:${field_index},
    desc: "${field.comments[0]}"
    };
</#list>


}
