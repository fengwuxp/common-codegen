import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';
import './${name?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}.reflectable.dart';

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        <#if !val.packagePath?starts_with("package:")>
            import '${customize_method.pathoResolve(packagePath,val.packagePath)}.dart';
        </#if>
    </#list>
</#if>

<#if comments??>
    <#list comments as cmment>
     /// ${cmment}
    </#list>
</#if>

<#list annotations as annotation>
    @${annotation.name}(<#list annotation.namedArguments as name,val>${name}:${val},</#list>)
</#list>
class ${name} extends FeignProxyClient {



        <#list methodMetas as method>

            <#list method.comments as cmment>
               /// ${cmment_index+1}:${cmment}
            </#list>
            <#list method.annotations as annotation>
                @${annotation.name}(<#list annotation.namedArguments as name,val>${name}:${val},</#list>)
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
                     @${annotation.name}(<#list annotation.positionArguments as item>${item}</#list>)
                  </#if>
              </#if>  ${customize_method.combineType(paramType.typeVariables)} ${paramName},
           </#list>
            <#assign returnType=returnTypes[0] />
             [UIOptions feignOptions]) {
               return this.delegateInvoke<${customize_method.combineType(returnTypes)}>("${method.name}",
                   [<#list params as paramName,paramType>${paramName},</#list>],
                    feignOptions: feignOptions,
            <#--基础类型的不生成 serializer相关参数,目前最多支持到3个泛型变量 -->
                <#if returnTypes?size<4 &&  !( returnType.name=="Object" ||
                         returnType.name=="dynamic"||
                         returnType.name=="void"||
                         returnType.name=="String"||
                         returnType.name=="num"||
                         returnType.name=="bool"||
                         returnType.name=="double" ||
                         returnType.name=="int" )>
                    serializer: BuiltValueSerializable(
                    <#if !returnTypes[0].name?starts_with("Built")>
                        serializer: ${returnType.name}.serializer,
                    </#if>
                    <#if (returnTypes?size == 2) >
                        specifiedType: FullType(${returnType.name}, [FullType(${returnTypes[1].name})])
                    </#if>
                    <#if (returnTypes?size == 3) >
                        specifiedType: FullType(${returnType.name}, [FullType(${returnTypes[1].name},[FullType(${returnTypes[2].name})])])
                    </#if>
                    )
                </#if>
               );
            }
        </#list>
}

void main() {
  initializeReflectable();
}


final ${name?uncap_first} = ${name}();
