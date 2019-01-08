
import {Enum} from "oak_weex_common/src/enums/Enum";

/**
<#list comments as cmment>
 * ${cmment}
</#list>
**/

export default class ${name}{

   constructor() {}

<#list filedMetas as field>
    public static readonly ${field.name}:Enum={
      name:"${field.name}",
      ordinal:${field_index},
      desc: "${field.comments[0]}"
    };
</#list>


}