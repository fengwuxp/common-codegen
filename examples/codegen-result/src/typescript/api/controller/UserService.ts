import {PostMapping} from "common_fetch/src/annotations/mapping/PostMapping";
import {GetMapping} from "common_fetch/src/annotations/mapping/GetMapping";
import {DeleteMapping} from "common_fetch/src/annotations/mapping/DeleteMapping";
import {PutMapping} from "common_fetch/src/annotations/mapping/PutMapping";
import {PatchMapping} from "common_fetch/src/annotations/mapping/PatchMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";
import {MediaType} from "common_fetch/src/constant/http/MediaType";

    import {DeleteUserReq} from "../req/DeleteUserReq";
    import {GetUserReq} from "../req/GetUserReq";
    import {GetUserListReq} from "../req/GetUserListReq";
    import {User} from "../domain/User";
    import {PutUserReq} from "../domain/PutUserReq";
    import {SampleReq} from "../req/SampleReq";

/**
    * 1:接口的请求方法为：POST
**/

    @Feign({
        value:'/users',
    })
 class UserService{

    /**
        * 1:删除用户
        * 2:接口的请求方法为：DELETE
        * 3:返回值在java中的类型为：String
    **/
        @DeleteMapping({
            value:'/{id}',
            produces:[MediaType.FORM_DATA],
        })
    deleteUser:(req: DeleteUserReq, option?: FetchOptions) => Promise<string>;
    /**
        * 1:获取用户详细信息
        * 2:接口的请求方法为：GET
        * 3:返回值在java中的类型为：User
    **/
        @GetMapping({
            value:'/{id}',
            produces:[MediaType.FORM_DATA],
        })
    getUser:(req: GetUserReq, option?: FetchOptions) => Promise<User>;
    /**
        * 1:获取用户列表
        * 2:接口的请求方法为：GET
        * 3:返回值在java中的类型为：List
        * 4:返回值在java中的类型为：User
    **/
        @GetMapping({
            produces:[MediaType.FORM_DATA],
        })
    getUserList:(req: GetUserListReq, option?: FetchOptions) => Promise<Array<User>>;
    /**
        * 1:创建用户
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：String
    **/
        @PostMapping({
        })
    postUser:(req: User, option?: FetchOptions) => Promise<string>;
    /**
        * 1:更新用户详细信息
        * 2:接口的请求方法为：PUT
        * 3:返回值在java中的类型为：String
    **/
        @PutMapping({
            value:'/{id}',
            produces:[MediaType.FORM_DATA],
        })
    putUser:(req: PutUserReq, option?: FetchOptions) => Promise<string>;
    /**
        * 1:sample
        * 2:接口的请求方法为：GET
        * 3:返回值在java中的类型为：String
    **/
        @GetMapping({
            produces:[MediaType.FORM_DATA],
        })
    sample:(req: SampleReq, option?: FetchOptions) => Promise<string>;
}

export default new UserService();