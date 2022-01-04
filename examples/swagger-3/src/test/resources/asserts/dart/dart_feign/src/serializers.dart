// Copyright (c) 2015, Google Inc. Please see the AUTHORS file for details.
// All rights reserved. Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

library serializers;

import 'package:built_collection/built_collection.dart';
import 'package:built_value/serializer.dart';
import 'package:built_value/standard_json_plugin.dart';
import 'package:fengwuxp_dart_openfeign/src/built/date_time_serializer.dart';

            import './evt/base_evt.dart';
            import './domain/base_example.dart';
            import './domain/base_info.dart';
            import './evt/base_query_evt.dart';
            import './evt/create_order_evt.dart';
            import './evt/example_dto.dart';
            import './example_dto.dart';
            import './domain/order.dart';
            import './resp/page_info.dart';
            import './evt/query_order_evt.dart';
            import './enums/sex.dart';
            import './domain/user.dart';

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
        ExampleDTO,
        ExampleDto,
        Order,
        PageInfo,
        QueryOrderEvt,
        Sex,
        User,
])

final Serializers serializers = (_$serializers.toBuilder()
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(BuiltList,[FullType(BuiltList,[FullType(BuiltList,[FullType(String)])])])]),
     () => ListBuilder<BuiltList<BuiltList<BuiltList<String>>>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(BuiltList,[FullType(BuiltList,[FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(String)])])])])]),
     () => ListBuilder<BuiltList<BuiltList<BuiltMap<String,BuiltList<String>>>>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(BuiltList,[FullType(BuiltList,[FullType(User)])])]),
     () => ListBuilder<BuiltList<BuiltList<User>>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(BuiltList,[FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(String)])])])]),
     () => ListBuilder<BuiltList<BuiltMap<String,BuiltList<String>>>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(BuiltList,[FullType(User)])]),
     () => ListBuilder<BuiltList<User>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(User)])])]),
     () => ListBuilder<BuiltMap<String,BuiltList<User>>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(BuiltMap,[FullType(int),FullType(String)])]),
     () => ListBuilder<BuiltMap<int,String>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(Order)]),
     () => ListBuilder<Order>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(PageInfo,[FullType(BuiltList,[FullType(BuiltList,[FullType(User)])])])]),
     () => ListBuilder<PageInfo<BuiltList<BuiltList<User>>>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(PageInfo,[FullType(BuiltList,[FullType(User)])])]),
     () => ListBuilder<PageInfo<BuiltList<User>>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(PageInfo,[FullType(User)])]),
     () => ListBuilder<PageInfo<User>>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(String)]),
     () => ListBuilder<String>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(User)]),
     () => ListBuilder<User>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(bool)]),
     () => ListBuilder<bool>())
    ..addBuilderFactory(
    const FullType(BuiltList,[FullType(int)]),
     () => ListBuilder<int>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(Sex),FullType(BuiltList,[FullType(PageInfo,[FullType(BuiltList,[FullType(User)])])])]),
     () => MapBuilder<Sex,BuiltList<PageInfo<BuiltList<User>>>>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(BuiltList,[FullType(BuiltList,[FullType(BuiltList,[FullType(String)])])])])]),
     () => MapBuilder<String,BuiltList<BuiltList<BuiltList<BuiltList<String>>>>>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(PageInfo,[FullType(User)])])]),
     () => MapBuilder<String,BuiltList<PageInfo<User>>>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(User)])]),
     () => MapBuilder<String,BuiltList<User>>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(String),FullType(BuiltList,[FullType(bool)])]),
     () => MapBuilder<String,BuiltList<bool>>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(String),FullType(Object)]),
     () => MapBuilder<String,Object>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(String),FullType(int)]),
     () => MapBuilder<String,int>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(int),FullType(BuiltList,[FullType(PageInfo,[FullType(BuiltList,[FullType(BuiltList,[FullType(User)])])])])]),
     () => MapBuilder<int,BuiltList<PageInfo<BuiltList<BuiltList<User>>>>>())
    ..addBuilderFactory(
    const FullType(BuiltMap,[FullType(int),FullType(String)]),
     () => MapBuilder<int,String>())
    ..addBuilderFactory(
    const FullType(PageInfo,[FullType(Order)]),
     () => PageInfoBuilder<Order>())
    ..addBuilderFactory(
    const FullType(PageInfo,[FullType(User)]),
     () => PageInfoBuilder<User>())
..addPlugin(StandardJsonPlugin())
..add(DateTimeMillisecondsSerializer()))
.build();
