// Copyright (c) 2015, Google Inc. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

library serializers;

import 'package:built_collection/built_collection.dart';
import 'package:built_value/serializer.dart';
import 'package:built_value/standard_json_plugin.dart';
import 'package:fengwuxp_dart_openfeign/src/built/date_time_serializer.dart';

            import './model/evt/base_evt.dart';
            import './enums/domain/base_example.dart';
            import './model/domain/base_info.dart';
            import './model/evt/base_query_evt.dart';
            import './model/evt/create_order_evt.dart';
            import './enums/example_enum.dart';
            import './model/domain/order.dart';
            import './model/resp/page_info.dart';
            import './model/evt/query_order_evt.dart';
            import './enums/sex.dart';
            import './model/domain/user.dart';

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
        BaseEvt,
        BaseExample,
        BaseInfo,
        BaseQueryEvt,
        CreateOrderEvt,
        ExampleEnum,
        Order,
        PageInfo,
        QueryOrderEvt,
        Sex,
        User,
])

final Serializers serializers = (_$serializers.toBuilder()
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(Object)]),
     () => ListBuilder<Object>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(Order)]),
     () => ListBuilder<Order>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(String)]),
     () => ListBuilder<String>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(User)]),
     () => ListBuilder<User>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(Sex),FullType(Sex)]),
     () => MapBuilder<Sex,Sex>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(Sex),FullType(int)]),
     () => MapBuilder<Sex,int>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(String),FullType(Object)]),
     () => MapBuilder<String,Object>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(String),FullType(Sex)]),
     () => MapBuilder<String,Sex>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(String),FullType(User)]),
     () => MapBuilder<String,User>())
    ..addBuilderFactory(
    const FullType(PageInfo,[FullType(Object)]),
     () => PageInfoBuilder<Object>())
    ..addBuilderFactory(
    const FullType(PageInfo,[FullType(Order)]),
     () => PageInfoBuilder<Order>())
    ..addBuilderFactory(
    const FullType(PageInfo,[FullType(String)]),
     () => PageInfoBuilder<String>())
..addPlugin(StandardJsonPlugin())
..add(DateTimeMillisecondsSerializer()))
.build();
