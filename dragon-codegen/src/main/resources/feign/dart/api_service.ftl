

<#--import 'package:fengwuxp_dart_openfeign/index.dart';-->

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        <#if !val.packagePath?starts_with("package:")>
            import '${customize_method.pathoResolve(packagePath,val.packagePath)}';
        </#if>
    </#list>
</#if>

<#if comments??>
    <#list comments as cmment>
     /// ${cmment}
    </#list>
</#if>

<#list annotations as annotation>
    @${annotation.name}({
    <#list annotation.namedArguments as name,val>
        ${name}:${val},
    </#list>
    })
</#list>
class ${name} extends FeignProxyClient {



        <#list methodMetas as method>

            <#list method.comments as cmment>
               /// ${cmment_index+1}:${cmment}
            </#list>
            <#list method.annotations as annotation>
                @${annotation.name}(
                <#list annotation.namedArguments as name,val>
                    ${name}:${val},
                </#list>
                )
            </#list>
            <#assign returnTypes=method.returnTypes/>
            Future<${customize_method.combineType(returnTypes)}>  ${method.name}(
            <#--参数遍历-->
            <#assign params=method.params/>
            <#assign paramAnnotations=method.paramAnnotations/>
            <#list params as paramName,paramType>
                <#assign paramAnnotation= paramAnnotations[paramName]/>
              <#if (paramAnnotation?size>0)>
                  <#assign annotation= paramAnnotation[0]/>
                 <#if annotation.positionArguments??>
                     @${annotation.name}(
                     <#list  annotation.positionArguments as item>
                         ${item},
                     </#list>
                     )
                 </#if>
              </#if>  ${customize_method.combineType(paramType.typeVariables)} ${paramName},
           </#list>
             [UIOptions feignOptions]) {
               return this.delegateInvoke<${customize_method.combineType(returnTypes)}>("${method.name}",
                [
            <#list params as paramName,paramType>
                ${paramName},
            </#list>
                ],
                    feignOptions: feignOptions,
                    serializer: BuiltValueSerializable(
                         serializer: ${returnTypes[0]}.serializer,
                        <#if (returnTypes?size > 1) >
                            specifiedType: FullType(${returnTypes[0].name}, [FullType(${returnTypes[1].name})]))
                        </#if>

            ));
            };
        </#list>
}

final ${name} = ${name}();
