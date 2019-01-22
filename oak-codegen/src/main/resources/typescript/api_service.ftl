import {RequestMapping} from "common_fetch/src/annotations/mapping/RequestMapping";
import {PostMapping} from "common_fetch/src/annotations/mapping/PostMapping";
import {GetMapping} from "common_fetch/src/annotations/mapping/GetMapping";
import {DeleteMapping} from "common_fetch/src/annotations/mapping/DeleteMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";
import {MediaType} from "common_fetch/src/constant/http/MediaType";

<#import "../common/customize_method.ftl" as customize_method/>

<#--依赖导入处理-->
<#list dependencies as key,val >
    <#--import {${key}} from "@api${val.packagePath}";-->
    import {${key}} from "${customize_method.pathoResolve(packagePath,val.packagePath)}";
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
 class ${name}{

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
    ${method.name}:(req: ${method.params["req"].name}, option?: FetchOptions) => ${customize_method.combineType(method.returnTypes)};
</#list>
}

export default new ${name}();