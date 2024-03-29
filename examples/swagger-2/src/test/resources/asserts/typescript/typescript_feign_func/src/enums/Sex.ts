/* tslint:disable */
  import {Enum} from "fengwuxp-typescript-feign";

      /**
          * 性别
          * 这是一个性别的枚举
      **/
  export class Sex{

  constructor() {}
        /**
          *男
          *男的
          *字段在java中的类型为：Sex
        **/
      public static readonly MAN:Enum={
      name:"MAN",
      ordinal:0,
       desc: "男"
      };
        /**
          *女
          *女的
          *字段在java中的类型为：Sex
        **/
      public static readonly WOMAN:Enum={
      name:"WOMAN",
      ordinal:1,
       desc: "女"
      };
        /**
          *未知
          *???
          *字段在java中的类型为：Sex
        **/
      public static readonly NONE:Enum={
      name:"NONE",
      ordinal:2,
       desc: "未知"
      };
  }
