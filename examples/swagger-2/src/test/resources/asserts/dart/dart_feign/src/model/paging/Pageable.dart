import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './Sort.dart';

    part 'pageable.g.dart';



abstract class Pageable implements Built<Pageable, PageableBuilder>, JsonSerializableObject {

Pageable._();

factory Pageable([Function(PageableBuilder) updates]) = _$Pageable;

        @BuiltValueField(wireName: 'paged')
        bool get paged;
        @BuiltValueField(wireName: 'unpaged')
        bool get unpaged;
        @BuiltValueField(wireName: 'offset')
        int get offset;
        @BuiltValueField(wireName: 'pageSize')
        int get pageSize;
        @BuiltValueField(wireName: 'pageNumber')
        int get pageNumber;
        @BuiltValueField(wireName: 'sort')
        Sort get sort;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(Pageable.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<Pageable> get serializer => _$pageableSerializer;

static Pageable? formJson(String json) {
return serializers.deserializeWith(Pageable.serializer, jsonDecode(json));
}

}
