import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './order.dart';
            import '../../enums/sex.dart';
            import '../../enums/example_enum.dart';
            import '../../serializers.dart';

    part 'user.g.dart';

        /// 用户


abstract class User implements Built<User, UserBuilder>, JsonSerializableObject {

User._();

factory User([Function(UserBuilder) updates]) = _$User;

            /// 属性说明：id，示例输入：
            /// 字段在java中的类型为：Long
        @BuiltValueField(wireName: 'id')
        int? get id;
            /// 属性说明：名称，示例输入：
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'name')
        String? get name;
            /// 属性说明：年龄，示例输入：
            /// 字段在java中的类型为：Integer
        @BuiltValueField(wireName: 'age')
        int? get age;
            /// 属性说明：订单列表，示例输入：
            /// 字段在java中的类型为：List
            /// 字段在java中的类型为：Order
        @BuiltValueField(wireName: 'orderList')
        BuiltList<Order>? get orderList;
            /// 属性说明：性别，示例输入：
            /// 字段在java中的类型为：Sex
        @BuiltValueField(wireName: 'sex')
        Sex? get sex;
            /// 属性说明：其他，示例输入：
            /// 字段在java中的类型为：Map
            /// 字段在java中的类型为：String
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'other')
        BuiltMap<String,String>? get other;
            /// 属性说明：其他2，示例输入：
            /// 字段在java中的类型为：Map
            /// 字段在java中的类型为：Object
            /// 字段在java中的类型为：Object
        @BuiltValueField(wireName: 'other2')
        BuiltMap<Object,Object>? get other2;
            /// 属性说明：list，示例输入：
            /// 字段在java中的类型为：List
            /// 字段在java中的类型为：Object
        @BuiltValueField(wireName: 'list')
        BuiltList<Object>? get list;
            /// 字段在java中的类型为：List
            /// 字段在java中的类型为：Object
        @BuiltValueField(wireName: 'list2')
        BuiltList<Object>? get list2;
            /// 属性说明：myFriends，示例输入：
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'myFriends')
        String? get myFriends;
            /// 属性说明：example enum，示例输入：
            /// 字段在java中的类型为：ExampleEnum
        @BuiltValueField(wireName: 'exampleEnum')
        ExampleEnum? get exampleEnum;
            /// 字段在java中的类型为：Boolean
        @BuiltValueField(wireName: 'boy')
        bool? get boy;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(User.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<User> get serializer => _$userSerializer;

static User? formJson(String json) {
return serializers.deserializeWith(User.serializer, jsonDecode(json));
}

}
