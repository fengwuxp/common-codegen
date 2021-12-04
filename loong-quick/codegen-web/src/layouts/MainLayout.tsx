import React, {useEffect, useState} from 'react';

import type {ProSettings} from '@ant-design/pro-layout';
import ProLayout, {SettingDrawer} from '@ant-design/pro-layout';
import {BasicLayoutProps} from "@ant-design/pro-layout/lib/BasicLayout";
import Logo from '@/assets/logo.svg';
import SvgIcon from "@/components/icon/SvgIcon";
import UserService from "@/feign/user/UserService";
import {MenuDataItem} from "@umijs/route-utils";
import {AppRouter} from "@/AppRouter";
import {UserDetails} from "@/feign/user/info/UserDetails";
import menus from "./Menus";
import GlobalHeaderRight from "@/components/antd-pro/RightContent";

const defaultProps: BasicLayoutProps = {
    logo: <SvgIcon src={Logo} size={40} color={"#ff0000"}/>,
    title: "Codegen",
    menu: {
        request: async (params: Record<string, any>, defaultMenuData: MenuDataItem[]): Promise<MenuDataItem[]> => menus
    }
}

export default (props) => {
    const [loginUser, setLoginUser] = useState<UserDetails>(null);
    useEffect(() => {
        UserService.getCurrentUserDetails().then((loginUser) => {
            setLoginUser({
                avatar:"https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png",
                ...loginUser
            })
        });
        return () => {
        }
    }, [])

    const {children, location} = props;
    const [settings, setSetting] = useState<Partial<ProSettings> | undefined>({
        fixSiderbar: true
    });
    return (
        <div id="codegen-pro-layout"
             style={{height: '100vh'}}>
            <ProLayout
                {...defaultProps}
                location={location}
                waterMarkProps={{
                    content: loginUser?.username
                }}
                onMenuHeaderClick={(e) => console.log(e)}
                menuItemRender={(item, dom) => (
                    <a onClick={() => {
                        AppRouter.push(item.path);
                    }}
                    >
                        {dom}
                    </a>
                )}
                rightContentRender={() => (
                    <GlobalHeaderRight currentUser={loginUser} menu={true} settings={settings}/>
                )}
                {...settings}
            >
                {children}
            </ProLayout>
            <SettingDrawer
                pathname={location.pathname}
                getContainer={() => document.getElementById('codegen-pro-layout')}
                settings={settings}
                onSettingChange={(changeSetting) => {
                    setSetting(changeSetting);
                }}
                disableUrlParams
            />
        </div>
    );
};
