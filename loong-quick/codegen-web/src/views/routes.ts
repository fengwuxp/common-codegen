import AsyncLoading from "@/components/loading/AsyncLoading";
import {AuthenticatedRouteConfig} from "@/components/route/PrivateRoute";

export const routes: Array<AuthenticatedRouteConfig> = [
    {
        path: "/user",
        exact: false,
        requiredAuthentication: false,
        component: AsyncLoading(() => import('@/layouts/UserLayout')),
        routes: [
            {
                path: '/login',
                exact: true,
                component: AsyncLoading(() => import('@/views/user/LoginView')),
            },
            {
                path: "*",
                exact: false,
                component: AsyncLoading(() => import("@/views/404"))
            }
        ]
    },
    {
        path: '/',
        exact: false,
        requiredAuthentication: true,
        component: AsyncLoading(() => import('@/layouts/MainLayout')),
        routes: [
            {
                path: "/home",
                exact: true,
                component: AsyncLoading(() => import("@/views/home/index"))
            },
            {
                path: "/code_repositories",
                exact: true,
                component: AsyncLoading(() => import("@/views/repositories/CodeRepositoriesListView"))
            },
            {
                path: "*",
                exact: false,
                component: AsyncLoading(() => import("@/views/404"))
            }
        ]
    }
];


