import {DefaultFooter, getPageTitle, MenuDataItem,} from '@ant-design/pro-layout';
import {Helmet} from 'react-helmet';
import {Link} from "react-router-dom";
import React from 'react';
import logo from '../assets/logo.svg';
import styles from './user.layout.module.less';
import {GithubOutlined} from "@ant-design/icons";

export interface UserLayoutProps {
    breadcrumbNameMap: {
        [path: string]: MenuDataItem;
    };
    location: Location;
}

const UserLayout: React.FC<UserLayoutProps> = (props) => {
    const {
        children,
        location = {
            pathname: '',
        },
    } = props;

    console.log("props", props);

    const title = getPageTitle({
        pathname: location.pathname,
        ...props,
        title: "登录"
    });

    return <>
        <Helmet>
            <title>{title}</title>
            <meta name="description" content={title}/>
        </Helmet>

        <div className={styles.container}>
            <div className={styles.lang}>11</div>
            <div className={styles.content}>
                <div className={styles.top}>
                    <div className={styles.header}>
                        <Link to={"/"}>
                            <img alt="logo" className={styles.logo} src={logo}/>
                            <span className={styles.title}>Loong Codegen</span>
                        </Link>
                    </div>
                    <div className={styles.desc}>为高效开发而生</div>
                </div>
                {children}
            </div>
            <DefaultFooter copyright={"2018 fengwuxp"} links={[{
                key: "codegen",
                title: <div><span>Codegen</span> <GithubOutlined style={{margin: "0 10px"}}/><span>专注代码生成</span>
                </div>,
                href: "https://github.com/fengwuxp/common-codegen",
                blankTarget: true
            }]}/>
        </div>
    </>
};

export default UserLayout;
