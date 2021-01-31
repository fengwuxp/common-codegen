import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';
<#--import './${name?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}.reflectable.dart';-->

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        <#if !val.packagePath?starts_with("package:")>
          import '${customizeMethod.pathResolve(packagePath,val.packagePath)}.dart';
        </#if>
    </#list>
</#if>


<#if (comments?size>0)>
    <#list comments as cmment>
      <#if cmment??>
      /// ${cmment}
      </#if>
    </#list>
</#if>
@Feign
<#list annotations as annotation>
  @${annotation.name}(<#list annotation.namedArguments as name,val>${name}:${val},</#list>)
</#list>
class ${name} extends FeignProxyClient {

${name}() : super() {

}

<#list methodMetas as method>

    <#list method.comments as cmment>
      <#if cmment??>
      /// <#if !(cmment?starts_with("@")||cmment?starts_with("<"))>${cmment_index+1}:</#if>${cmment}
      </#if>
    </#list>
    <#list method.annotations as annotation>
        <#if annotation.name=='Signature'>
          @${annotation.name}(${annotation.namedArguments['fields']})
        <#else >
          @${annotation.name}(<#list annotation.namedArguments as name,val>${name}:${val},</#list>)
        </#if>
    </#list>
    <#assign returnTypes=method.returnTypes/>
  Future<${customizeMethod.combineType(returnTypes)}>  ${method.name}(
<#--参数遍历-->
    <#assign params=method.params/>
    <#assign paramAnnotations=method.paramAnnotations/>
    <#list params as paramName,paramType>
        <#assign paramAnnotation= paramAnnotations[paramName]/>
        <#if (paramAnnotation?size>0)>
            <#assign annotation= paramAnnotation[0]/>
            <#assign len=annotation.namedArguments?size />
            <#assign currentIndex=0 />
          @${annotation.name}(<#list annotation.namedArguments as name,val>${name} = ${val!""} <#if currentIndex<len-1>,</#if><#assign currentIndex=currentIndex+1 /></#list>)
        </#if>
        ${customizeMethod.combineType(paramType.typeVariables)} ${paramName},
    </#list>
    <#assign returnType=returnTypes[0] />
  [UIOptions feignOptions]) {
  return this.delegateInvoke<${customizeMethod.combineType(returnTypes)}>("${method.name}",
  [<#list params as paramName,paramType>${paramName},</#list>],
<#--基础类型的不生成 serializer相关参数,目前最多支持到3个泛型变量 -->
    <#assign useSerializer=false>
    <#assign isBaseType=
    returnType.name=="String"||
    returnType.name=="num"||
    returnType.name=="bool"||
    returnType.name=="double" ||
    returnType.name=="int"/>
    <#if !( returnType.name=="Object" ||
    returnType.name=="dynamic"||
    returnType.name=="void" )>
        <#assign specifiedType=customizeMethod.combineDartFullType(returnTypes)!""/>
        <#if (specifiedType?length > 0 || !returnTypes[0].name?starts_with("Built"))>
            <#assign useSerializer=true>
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
            <#if !returnTypes[0].name?starts_with("Built") && !isBaseType>
              serializer: ${returnType.name}.serializer,
            </#if>
            <#if (specifiedType?length > 0 && !isBaseType)>
              specifiedType:${specifiedType}
            </#if>
            <#if isBaseType>
              specifiedType:FullType(${returnTypes[0].name})
            </#if>
          )
        </#if>
    </#if>
    <#if !useSerializer>feignOptions: feignOptions</#if>
  );
  }
</#list>
}


final ${name?uncap_first} = ${name}();
