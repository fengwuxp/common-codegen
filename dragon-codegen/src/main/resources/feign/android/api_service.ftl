import com.oaknt.common.service.support.model.ServiceQueryResp;
import com.oaknt.common.service.support.model.ServiceResp;
import io.reactivex.Observable;
import retrofit2.http.*;

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        import ${customize_method.pathoResolve(packagePath,val.packagePath)};
    </#list>
</#if>

<#if comments??>
    /**
    <#list comments as cmment>
        * ${cmment}
    </#list>
    **/
</#if>

<#list annotations as annotation>
    <#assign len=annotation.namedArguments?size />
    <#assign currentIndex=0 />
    @${annotation.name}(
    <#list annotation.namedArguments as name,val>
        ${name} = ${val} <#if currentIndex<len-1>,</#if>
        <#assign currentIndex=currentIndex+1 />
    </#list>
    )
</#list>
public interface ${name}{

<#list methodMetas as method>
    /**
    <#list method.comments as cmment>
        * ${cmment_index+1}:${cmment}
    </#list>
    **/
    <#list method.annotations as annotation>
        <#assign len=annotation.namedArguments?size />
        <#assign currentIndex=0 />
        @${annotation.name}(
        <#list annotation.namedArguments as name,val>
            ${name} = ${val} <#if currentIndex<len-1>,</#if>
            <#assign currentIndex=currentIndex+1 />
        </#list>
        )
    </#list>
    ${customize_method.combineType(method.returnTypes)}  ${method.name} (@Body ${method.params["req"].name} req);
</#list>
}
