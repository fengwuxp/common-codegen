// https://umijs.org/config/
// import routes from "../.spring/routes";
import {defineConfig} from 'umi';
import defaultSettings from './defaultSettings';
import routes from './routes';

const {REACT_APP_ENV} = process.env;

export default defineConfig({
  hash: false,
  antd: {},
  locale: {
    // default zh-CN
    default: 'zh-CN',
    // default true, when it is true, will use `navigator.language` overwrite default
    antd: true,
    baseNavigator: true,
  },
  dynamicImport: {
    loading: '@/components/pageloading/index',
  },
  targets: {
    ie: 11,
  },
  // umi routes: https://umijs.org/zh/guide/router.html
  routes: [
    {
      path: '/',
      component: '../layouts/BlankLayout',
      routes: [
        {
          path: '/user',
          component: '../layouts/UserLayout',
          wrappers: ['../DefaultPrivateRoute'],
          routes: [
            {
              path: '/user',
              redirect: '/user/login',
            },
            {
              name: '登录',
              icon: 'crown',
              path: '/user/login',
              component: './user/LoginView',
            },
            {
              component: '404',
            },
          ],
        },
        {
          path: '/',
          component: '../layouts/BasicLayout',
          wrappers: ['../DefaultPrivateRoute'],
          routes: routes,
        },
        {
          component: '404',
        },
      ],
    },
  ],
  theme: {
    // ...darkTheme,
    'primary-color': defaultSettings.primaryColor,
  },
  ignoreMomentLocale: true,

  manifest: {
    basePath: '/',
  },
  nodeModulesTransform: {
    type: 'none',
  },
  fastRefresh: false,
});
