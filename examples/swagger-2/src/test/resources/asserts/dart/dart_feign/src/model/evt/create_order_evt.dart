import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './base_evt.dart';

    part 'create_order_evt.g.dart';

        /// 创建订单


abstract class CreateOrderEvt implements Built<CreateOrderEvt, CreateOrderEvtBuilder>, JsonSerializableObject {

CreateOrderEvt._();

factory CreateOrderEvt([Function(CreateOrderEvtBuilder) updates]) = _$CreateOrderEvt;

            /// 属性说明：订单ns，示例输入：test method
            /// sn 约束条件：输入字符串的最小长度为：0，输入字符串的最大长度为：50
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'sn')
        String get sn;
            /// 属性说明：订单总价，示例输入：
            /// totalAmount 约束条件：为必填项，不能为空
            /// 字段在java中的类型为：Integer
        @BuiltValueField(wireName: 'totalAmount')
        int get totalAmount;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(CreateOrderEvt.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<CreateOrderEvt> get serializer => _$createOrderEvtSerializer;

static CreateOrderEvt? formJson(String json) {
return serializers.deserializeWith(CreateOrderEvt.serializer, jsonDecode(json));
}

}
