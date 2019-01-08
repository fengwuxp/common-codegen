
import {Enum} from "oak_weex_common/src/enums/Enum";

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