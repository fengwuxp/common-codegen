import {IRoute} from "@umijs/core";


const routes: IRoute[] = [
  {
    name: '首页',
    icon: 'smile',
    path: '/',
    routes: [
      {path: '/home', component: '@/pages/index'},
    ]
  }
]


export default routes
