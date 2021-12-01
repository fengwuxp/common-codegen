import React from "react";
import {Action, Location} from "history";
import {Router} from "react-router-dom";
import {routes} from "@/views/routes";
import {renderAppRoutes} from "@/ReanderRoutes";
import {history} from "@/AppRouter";


history.listen((location: Location, action: Action) => {
    console.log("路由变化", location.pathname, action);
})

const App = (pops) => {

    return <Router history={history}>
        {
            renderAppRoutes(routes, {
                isAuthenticated: (): Promise<void> => {
                    return Promise.resolve();
                },
                authenticationView: () => "/user/login"
            })
        }
    </Router>
}

export default App;
