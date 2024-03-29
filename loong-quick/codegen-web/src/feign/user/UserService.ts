import {Feign, FeignRequestOptions, GetMapping, HttpMediaType, PostMapping} from 'fengwuxp-typescript-feign';
import {LoginReq} from '@/feign/user/req/LoginReq';
import {UserDetails} from '@/feign/user/info/UserDetails';

/**
 * 类：例子服务
 * */

@Feign({
    value: '/',
})
class UserService {
    @PostMapping({
        value: '/login', produces: [HttpMediaType.FORM_DATA]
    })
    login: (req: LoginReq, option?: FeignRequestOptions,
    ) => Promise<void>;

    @PostMapping({
        value: '/logout',
    })
    logout: (req?, option?: FeignRequestOptions) => Promise<void>;


    @GetMapping({
        value: '/api/v1/authentication/details',
    })
    getCurrentUserDetails: (req?, option?: FeignRequestOptions) => Promise<UserDetails>
}

export default new UserService();
