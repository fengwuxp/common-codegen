import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import '../../serializers.dart';

    part 'page_info.g.dart';

        /// 分页对象


abstract class PageInfo<T> implements Built<PageInfo<T>, PageInfoBuilder<T>>, JsonSerializableObject {

PageInfo._();

factory PageInfo([Function(PageInfoBuilder<T>) updates]) = _$PageInfo<T>;

            /// 属性说明：响应集合列表，示例输入：
            /// 字段在java中的类型为：List
            /// 字段在java中的类型为：Object
        @BuiltValueField(wireName: 'records')
        BuiltList<T>? get records;
            /// 属性说明：查询页码，示例输入：
            /// 字段在java中的类型为：Integer
        @BuiltValueField(wireName: 'queryPage')
        int? get queryPage;
            /// 属性说明：查询大小，示例输入：
            /// 字段在java中的类型为：Integer
        @BuiltValueField(wireName: 'querySize')
        int? get querySize;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(PageInfo.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<PageInfo> get serializer => _$pageInfoSerializer;

static PageInfo? formJson(String json) {
return serializers.deserializeWith(PageInfo.serializer, jsonDecode(json));
}

}
