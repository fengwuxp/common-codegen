import {RequestMapping} from "common_fetch/src/annotations/mapping/RequestMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";
<#--创建这个对象保存好-->
<#assign combineType = "com.oaknt.codegen.freemarker.CombineTypeMethod"?new() />

<#--依赖导入处理-->
<#list dependencies as key,val >
    import {${key}} from "@/src/${val.packagePath}";
</#list>

/**
<#list comments as cmment>
    * ${cmment_index+1}:${cmment}
</#list>
**/

<#list annotations as annotation>
    @${annotation.name}({
    <#list annotation.namedArguments as name,val>
        ${name}:${val},
    </#list>
    })
</#list>
export default class ${name}{

<#list methodMetas as method>
    /**
    <#list method.comments as cmment>
        * ${cmment_index+1}:${cmment}
    </#list>
    **/
    <#list method.annotations as annotation>
        @${annotation.name}({
        <#list annotation.namedArguments as name,val>
            ${name}:${val},
        </#list>
        })
    </#list>
    ${method.name}:(req: ${method.params["req"].name}, option?: FetchOptions) => ${combineType(method.returnTypes)};
</#list>
}