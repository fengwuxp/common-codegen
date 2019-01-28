import {PostMapping} from "common_fetch/src/annotations/mapping/PostMapping";
import {GetMapping} from "common_fetch/src/annotations/mapping/GetMapping";
import {DeleteMapping} from "common_fetch/src/annotations/mapping/DeleteMapping";
import {PutMapping} from "common_fetch/src/annotations/mapping/PutMapping";
import {PatchMapping} from "common_fetch/src/annotations/mapping/PatchMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";
import {MediaType} from "common_fetch/src/constant/http/MediaType";

    import {GetUserListReq} from "../req/GetUserListReq";
    import {GetUserReq} from "../req/GetUserReq";
    import {DeleteUserReq} from "../req/DeleteUserReq";
    import {User} from "../domain/User";

/**
    * 1:接口的请求方法为：POST
**/

    @PostMapping({
        value:'/users',
    })
 class UserService{

    /**
        * 1:接口的请求方法为：PUT
        * 2:返回值在java中的类型为：String
    **/
        @PutMapping({
            value:'/{id}',
        })
    putUser:(req: User, option?: FetchOptions) => Promise<string>;
    /**
        * 1:接口的请求方法为：POST
        * 2:返回值在java中的类型为：String
    **/
        @PostMapping({
        })
    postUser:(req: User, option?: FetchOptions) => Promise<string>;
    /**
        * 1:接口的请求方法为：GET
        * 2:返回值在java中的类型为：List
        * 3:返回值在java中的类型为：User
    **/
        @GetMapping({
        })
    getUserList:(req: GetUserListReq, option?: FetchOptions) => Promise<Array<User>>;
    /**
        * 1:接口的请求方法为：GET
        * 2:返回值在java中的类型为：User
    **/
        @GetMapping({
            value:'/{id}',
        })
    getUser:(req: GetUserReq, option?: FetchOptions) => Promise<User>;
    /**
        * 1:接口的请求方法为：DELETE
        * 2:返回值在java中的类型为：String
    **/
        @DeleteMapping({
            value:'/{id}',
        })
    deleteUser:(req: DeleteUserReq, option?: FetchOptions) => Promise<string>;
}

export default new UserService();