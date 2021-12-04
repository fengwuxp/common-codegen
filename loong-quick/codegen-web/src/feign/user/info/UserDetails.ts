export interface UserDetails {

    id: number;

    /**
     * 姓名
     */
    name: string;

    /**
     * 昵称
     */
    nickName: string;

    avatar: string;

    /**
     * 手机号码
     */
    mobilePhone: string;

    /**
     * 邮箱
     */
    email: string;

    username: string;

    authorities: any[];

    enabled: boolean;
}
