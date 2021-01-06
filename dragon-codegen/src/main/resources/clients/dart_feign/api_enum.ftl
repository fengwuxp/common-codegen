import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';


part '${name?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}.g.dart';

<#if comments??>
    <#list comments as cmment>
      /// ${cmment}
    </#list>
</#if>
class ${name} extends EnumClass  {


static Serializer<${name}> get serializer => _${"$"}${name?uncap_first}Serializer;

<#list fieldMetas as field>
    <#list field.comments as cmment>
      /// ${cmment}
    </#list>
  static const ${name} ${field.name} = _${"$"}${field.name};
</#list>

const ${name}._(String name):super(name);

static BuiltSet<${name}> get values => _$values;

static ${name} valueOf(String name) => _$valueOf(name);
}
