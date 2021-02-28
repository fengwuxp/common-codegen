import {DefaultFooter, getMenuData, getPageTitle, MenuDataItem,} from '@ant-design/pro-layout';
import {Helmet, HelmetProvider} from 'react-helmet-async';
import React from 'react';
import {Link, useIntl} from 'umi';
import logo from '../assets/logo.svg';
import styles from './UserLayout.less';
import defaultSettings from '../../config/defaultSettings';
import {IRoute} from '@umijs/core';
import {GithubOutlined} from "@ant-design/icons";

export interface UserLayoutProps {
  breadcrumbNameMap: {
    [path: string]: MenuDataItem;
  };
  location: Location;

  route: IRoute;
}

const UserLayout: React.FC<UserLayoutProps> = (props) => {
  const {
    route = {
      routes: [],
    },
  } = props;

  const {routes = []} = route;
  const {
    children,
    location = {
      pathname: '',
    },
  } = props;
  const {formatMessage} = useIntl();
  const {breadcrumb} = getMenuData(routes);
  const title = getPageTitle({
    pathname: location.pathname,
    formatMessage,
    breadcrumb,
    ...props,
    title: defaultSettings.title,
  });

  return (
    <HelmetProvider>
      <Helmet>
        <title>{title}</title>
        <meta name="description" content={title}/>
      </Helmet>

      <div className={styles.container}>
        <div className={styles.lang}>11</div>
        <div className={styles.content}>
          <div className={styles.top}>
            <div className={styles.header}>
              <Link to="/">
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
          title: <div><span>Codegen</span> <GithubOutlined style={{margin: "0 10px"}}/><span>专注代码生成</span></div>,
          href: "https://github.com/fengwuxp/common-codegen",
          blankTarget: true
        }]}/>
      </div>
    </HelmetProvider>
  );
};

export default UserLayout;
