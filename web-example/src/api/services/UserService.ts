import {RequestMapping} from "common_fetch/src/annotations/mapping/RequestMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";

    import {User} from "@/src/api/domain/User";

/**
    * 1:用户服务
    * 2:接口的请求方法为：POST
**/

    @Feign({
        value:'/users',
    })
export default class UserService{

    /**
        * 1:创建用户
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：String
    **/
        @RequestMapping({
            method:RequestMethod.POST,
        })
    postUser:(req: User, option?: FetchOptions) => Promise<string>;
    /**
        * 1:获取用户列表
        * 2:接口的请求方法为：GET
        * 3:返回值在java中的类型为：List
        * 4:返回值在java中的类型为：User
    **/
        @RequestMapping({
            method:RequestMethod.GET,
        })
    getUserList:(req: GetUserListReq, option?: FetchOptions) => Promise<Array<User>>;
    /**
        * 1:获取用户详细信息
        * 2:接口的请求方法为：GET
        * 3:返回值在java中的类型为：User
    **/
        @RequestMapping({
            value:'/{id}',
            method:RequestMethod.GET,
        })
    getUser:(req: GetUserReq, option?: FetchOptions) => Promise<User>;
    /**
        * 1:删除用户
        * 2:接口的请求方法为：DELETE
        * 3:返回值在java中的类型为：String
    **/
        @RequestMapping({
            value:'/{id}',
            method:RequestMethod.DELETE,
        })
    deleteUser:(req: DeleteUserReq, option?: FetchOptions) => Promise<string>;
    /**
        * 1:更新用户详细信息
        * 2:接口的请求方法为：PUT
        * 3:返回值在java中的类型为：String
    **/
        @RequestMapping({
            value:'/{id}',
            method:RequestMethod.PUT,
        })
    putUser:(req: User, option?: FetchOptions) => Promise<string>;
}