import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';


part 'example_enum.g.dart';

      /// ExampleEnum
      /// 这是一个ExampleEnum
class ExampleEnum extends EnumClass  {


static Serializer<ExampleEnum> get serializer => _$exampleEnumSerializer;
/// 字段在java中的类型为：ExampleEnum
static const ExampleEnum MAN = _$MAN;
/// 女
/// @serialField 妹纸
/// 字段在java中的类型为：ExampleEnum
static const ExampleEnum WOMAN = _$WOMAN;
/// 字段在java中的类型为：ExampleEnum
static const ExampleEnum NONE = _$NONE;


const ExampleEnum._(String name):super(name);

static BuiltSet<ExampleEnum> get values => _$values;

static ExampleEnum valueOf(String name) => _$valueOf(name);
}
