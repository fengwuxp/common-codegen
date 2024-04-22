import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';


part 'sex.g.dart';

      /// 性别
class Sex extends EnumClass  {


static Serializer<Sex> get serializer => _$sexSerializer;
/// 字段在java中的类型为：Sex
static const Sex MAN = _$MAN;
/// 字段在java中的类型为：Sex
static const Sex WOMAN = _$WOMAN;
/// 字段在java中的类型为：Sex
static const Sex NONE = _$NONE;


const Sex._(String name):super(name);

static BuiltSet<Sex> get values => _$values;

static Sex valueOf(String name) => _$valueOf(name);
}
