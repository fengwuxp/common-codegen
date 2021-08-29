// Copyright (c) 2015, Google Inc. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

library serializers;

import 'package:built_collection/built_collection.dart';
import 'package:built_value/serializer.dart';
import 'package:built_value/standard_json_plugin.dart';
import 'package:fengwuxp_dart_openfeign/src/built/date_time_serializer.dart';

<#if tags.dependencies??>
<#--依赖导入处理-->
    <#list tags.dependencies as val >
        <#if !val.packagePath?starts_with("package:")>
            import '${customizeMethod.pathResolve(packagePath,val.packagePath)}.dart';
        </#if>
    </#list>
</#if>

part 'serializers.g.dart';

/// Example of how to use built_value serialization.
///
/// Declare a top level [Serializers] field called serializers. Annotate it
/// with [SerializersFor] and provide a `const` `List` of types you want to
/// be serializable.
///
/// The built_value code generator will provide the implementation. It will
/// contain serializers for all the types asked for explicitly plus all the
/// types needed transitively via fields.
///
/// You usually only need to do this once per project.
@SerializersFor(const [
<#if tags.dependencies??>
<#--依赖导入处理-->
    <#list tags.dependencies as val >
        ${val.name},
    </#list>
</#if>
])

final Serializers serializers = (_$serializers.toBuilder()
<#list tags.builderFactories as factory>
    ..addBuilderFactory(
    ${factory.fullTypeCode},
    ${factory.functionCode}
</#list>
..addPlugin(StandardJsonPlugin())
..add(DateTimeMillisecondsSerializer()))
.build();
