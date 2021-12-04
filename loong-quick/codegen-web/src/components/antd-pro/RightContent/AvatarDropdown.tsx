import React from 'react';
import {LogoutOutlined, SettingOutlined, UserOutlined} from '@ant-design/icons';
import {Avatar, Menu, Spin} from 'antd';
import HeaderDropdown from '../HeaderDropdown';
import styles from './index.module.less';
import type {MenuInfo} from 'rc-menu/lib/interface';
import {UserDetails} from "@/feign/user/info/UserDetails";

export interface AvatarDropdownProps {
    menu?: boolean;
    currentUser?: UserDetails
};


const AvatarDropdown: React.FC<AvatarDropdownProps> = (props) => {
    const {menu, currentUser} = props;

    const onMenuClick = (event: MenuInfo) => {
        // const {key} = event;
    }

    const loading = (
        <span className={`${styles.action} ${styles.account}`}>
      <Spin size="small"
            style={{
                marginLeft: 8,
                marginRight: 8,
            }}
      />
    </span>
    );

    if (!currentUser) {
        return loading;
    }

    const menuHeaderDropdown = (
        <Menu className={styles.menu} selectedKeys={[]} onClick={onMenuClick}>
            {menu && (
                <Menu.Item key="center">
                    <UserOutlined/>
                    个人中心
                </Menu.Item>
            )}
            {menu && (
                <Menu.Item key="settings">
                    <SettingOutlined/>
                    个人设置
                </Menu.Item>
            )}
            {menu && <Menu.Divider/>}

            <Menu.Item key="logout">
                <LogoutOutlined/>
                退出登录
            </Menu.Item>
        </Menu>
    );
    return <HeaderDropdown overlay={menuHeaderDropdown}>
      <span className={`${styles.action} ${styles.account}`}>
        <Avatar size="small" className={styles.avatar} src={currentUser.avatar} alt="avatar"/>
        <span className={`${styles.name} anticon`}>{currentUser.name || currentUser.username}</span>
      </span>
    </HeaderDropdown>
};

export default AvatarDropdown;
