import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './Pageable.dart';
            import './Sort.dart';

    part 'page.g.dart';



abstract class Page<T> implements Built<Page<T>, PageBuilder<T>>, JsonSerializableObject {

Page._();

factory Page([Function(PageBuilder<T>) updates]) = _$Page<T>;

        @BuiltValueField(wireName: 'content')
        BuiltList<T> get content;
        @BuiltValueField(wireName: 'empty')
        bool get empty;
        @BuiltValueField(wireName: 'first')
        bool get first;
        @BuiltValueField(wireName: 'last')
        bool get last;
        @BuiltValueField(wireName: 'number')
        int get number;
        @BuiltValueField(wireName: 'numberOfElements')
        int get numberOfElements;
        @BuiltValueField(wireName: 'size')
        int get size;
        @BuiltValueField(wireName: 'totalElements')
        int get totalElements;
        @BuiltValueField(wireName: 'totalPages')
        int get totalPages;
        @BuiltValueField(wireName: 'pageable')
        Pageable get pageable;
        @BuiltValueField(wireName: 'sort')
        Sort get sort;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(Page.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<Page> get serializer => _$pageSerializer;

static Page? formJson(String json) {
return serializers.deserializeWith(Page.serializer, jsonDecode(json));
}

}
