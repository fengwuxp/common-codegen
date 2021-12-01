import {BrowserNavigatorAdapter, BrowserNavigatorContextAdapter} from 'fengwuxp-browser-router'
import {
    AbstractAppCommandRouter,
    AppRouterMapping,
    NavigatorDescriptorObject,
    RouteConfirmBeforeJumping,
    RouteMapping,
    RouterCommandMethod
} from 'fengwuxp-declarative-router-adapter'
import {createBrowserHistory} from "history";
import {RouteLoginViewProps} from "@/typings/AppRouteProps";


const basename = "/";

export const history = createBrowserHistory({
    basename
});

// 判断是否需要登录
const routeConfirmBeforeJumping: RouteConfirmBeforeJumping = (nextNavigator: NavigatorDescriptorObject) => true;


@AppRouterMapping({
    confirmBeforeJumping: () => routeConfirmBeforeJumping,
    navigatorContextAdapter: () => new BrowserNavigatorContextAdapter(history),
    navigatorAdapter: () => new BrowserNavigatorAdapter(history),
    pathPrefix: basename,
    autoJoinQueryString: false
})
class CodegenAppRouter extends AbstractAppCommandRouter {

    @RouteMapping('/home')
    home: RouterCommandMethod<any>;

    @RouteMapping('/user/login')
    login: RouterCommandMethod<RouteLoginViewProps>;
}

export const AppRouter: CodegenAppRouter = new CodegenAppRouter();
