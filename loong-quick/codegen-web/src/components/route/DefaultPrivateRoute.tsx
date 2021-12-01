import {Redirect, Route} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {AppRouterAuthenticator, PrivateRoute, PrivateRouteProps} from "./PrivateRoute";


const isFunction = (val) => {
    return typeof val == "function";
}

const DEFAULT_AUTHENTICATOR: AppRouterAuthenticator<any> = {
    isAuthenticated(): Promise<void> {
        return Promise.resolve();
    }
}

/**
 * 默认的私有的路由，需要登录
 * @param props
 * @constructor
 */
const DefaultPrivateRoute: PrivateRoute = (props: PrivateRouteProps) => {
    const {extraProps, component, ...routeProps} = props;
    const [authenticated, setAuthenticated] = useState(null);
    const routeAuthenticator = props.routeAuthenticator || DEFAULT_AUTHENTICATOR
    useEffect(() => {
        routeAuthenticator.isAuthenticated()
            .then(() => true)
            .catch(() => false)
            .then(setAuthenticated);

        return () => {
        }
    }, []);

    if (authenticated == null) {
        // TODO 等待登录状态
        return <div>loading</div>;
    }
    console.log("DefaultPrivateRoute authenticated", authenticated);
    if (!authenticated) {
        return <Redirect to={{
            pathname: isFunction(routeAuthenticator.authenticationView) ? routeAuthenticator.authenticationView() : "/login",
            state: {from: props.location}
        }} from={props.location.pathname}/>
    }

    const routeRenderFn = (renderComponentProps) => {
        if (isFunction(routeProps.render)) {
            return routeProps.render({...renderComponentProps, ...extraProps})
        }
        return <props.component {...renderComponentProps} {...extraProps}/>;
    }
    return <Route {...routeProps}
                  exact={routeProps.exact ?? true}
                  strict={routeProps.strict ?? true}
                  render={routeRenderFn}/>
}

export default DefaultPrivateRoute;
