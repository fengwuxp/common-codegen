import { ViewShowMode } from 'fengwuxp-routing-core';
import { IRoute } from 'umi';

const routes: IRoute[] = [
  {
    name: '/home/index',

    icon: 'smile',

    path: '/home',
    routes: [
      {
        name: '/home/index',

        icon: 'smile',

        path: '/home',
        routes: [
          {
            name: '/home/index',

            path: '/home/index',

            component: './home/index',
          },
        ],
      },
    ],
  },
];

export default routes;
