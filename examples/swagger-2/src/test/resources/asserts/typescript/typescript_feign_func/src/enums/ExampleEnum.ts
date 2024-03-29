/* tslint:disable */
  import {Enum} from "fengwuxp-typescript-feign";

      /**
          * ExampleEnum
          * 这是一个ExampleEnum
      **/
  export class ExampleEnum{

  constructor() {}
        /**
          *男的
          *字段在java中的类型为：ExampleEnum
        **/
      public static readonly MAN:Enum={
      name:"MAN",
      ordinal:0,
       desc: "男的"
      };
        /**
          *女
          *@serialField 妹纸
          *字段在java中的类型为：ExampleEnum
        **/
      public static readonly WOMAN:Enum={
      name:"WOMAN",
      ordinal:1,
       desc: "女"
      };
        /**
          *位置
          *字段在java中的类型为：ExampleEnum
        **/
      public static readonly NONE:Enum={
      name:"NONE",
      ordinal:2,
       desc: "位置"
      };
  }
