import {Space} from 'antd';
import {QuestionCircleOutlined} from '@ant-design/icons';
import React from 'react';
import AvatarDropdown, {AvatarDropdownProps} from './AvatarDropdown';
import HeaderSearch from '../HeaderSearch';
import styles from './index.module.less';
import {ProSettings} from "@ant-design/pro-layout/lib/defaultSettings";

export type SiderTheme = 'light' | 'dark';

export interface GlobalHeaderRightProps extends AvatarDropdownProps {
    settings: ProSettings;
}

const getHeaderRightClassname=(settings: ProSettings) =>{
    const {navTheme, layout} = settings;
    if ((navTheme === 'dark' && layout === 'top') || layout === 'mix') {
        return `${styles.right}  ${styles.dark}`;
    }
    return styles.right;
}

const GlobalHeaderRight: React.FC<GlobalHeaderRightProps> = (props) => {

    const {settings} = props;

    return (
        <Space className={getHeaderRightClassname(settings)}>
            <HeaderSearch
                className={`${styles.action} ${styles.search}`}
                placeholder="站内搜索"
                defaultValue={""}
                options={[
                    {label: <a href="https://umijs.org/zh/guide/umi-ui.html">umi ui</a>, value: 'umi ui'},
                    {
                        label: <a href="next.ant.design">Ant Design</a>,
                        value: 'Ant Design',
                    },
                    {
                        label: <a href="https://protable.ant.design/">Pro Table</a>,
                        value: 'Pro Table',
                    },
                    {
                        label: <a href="https://prolayout.ant.design/">Pro Layout</a>,
                        value: 'Pro Layout',
                    },
                ]}
                onSearch={value => {
                    console.log('input', value);
                }}
            />
            <span
                className={styles.action}
                onClick={() => {
                    window.open('https://pro.ant.design/docs/getting-started');
                }}
            >
        <QuestionCircleOutlined/>
      </span>
            <AvatarDropdown menu={props.menu} currentUser={props.currentUser}/>
        </Space>
    );
};
export default GlobalHeaderRight;
