import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';


    part 'sort.g.dart';



abstract class Sort implements Built<Sort, SortBuilder>, JsonSerializableObject {

Sort._();

factory Sort([Function(SortBuilder) updates]) = _$Sort;

        @BuiltValueField(wireName: 'unsorted')
        bool get unsorted;
        @BuiltValueField(wireName: 'sorted')
        bool get sorted;
        @BuiltValueField(wireName: 'empty')
        bool get empty;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(Sort.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<Sort> get serializer => _$sortSerializer;

static Sort? formJson(String json) {
return serializers.deserializeWith(Sort.serializer, jsonDecode(json));
}

}
