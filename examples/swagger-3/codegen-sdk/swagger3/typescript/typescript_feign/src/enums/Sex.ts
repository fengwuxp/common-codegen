/* tslint:disable */
  import {Enum} from "feign-client";

      /**
          * 性别
      **/
  export class Sex{

  constructor() {}
        /**
          *字段在java中的类型为：Sex
        **/
      public static readonly MAN:Enum={
      name:"MAN",
      ordinal:0,
       desc: "字段在java中的类型为：Sex"
      };
        /**
          *字段在java中的类型为：Sex
        **/
      public static readonly WOMAN:Enum={
      name:"WOMAN",
      ordinal:1,
       desc: "字段在java中的类型为：Sex"
      };
        /**
          *字段在java中的类型为：Sex
        **/
      public static readonly NONE:Enum={
      name:"NONE",
      ordinal:2,
       desc: "字段在java中的类型为：Sex"
      };
  }
