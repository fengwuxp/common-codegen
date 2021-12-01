import React from "react";
import * as ReactDOM from "react-dom";
import {I18nextProvider} from 'react-i18next';
import BrowserFeignConfigurationAdapter from "@/BrowserFeignConfigurationAdapter";
import App from "@/App";
import {Spin} from "antd";
import {feignConfigurationInitializer} from "feign-boot-starter";
import {setDefaultLoadingComponent} from "@/components/loading/AsyncLoading";
import i18n from '@/i18n';
import {AppRouter} from "@/AppRouter";

//  注册feign 代理
const feignConfig = feignConfigurationInitializer(new BrowserFeignConfigurationAdapter());

feignConfig.getHttpResponseEventListener().onUnAuthorized((response) => {
    console.log("onUnAuthorized", response);
    AppRouter.login();
})

setDefaultLoadingComponent(Spin)

// Render the top-level React component
ReactDOM.render(
    <I18nextProvider i18n={i18n}>
        <App/>
    </I18nextProvider>,
    document.getElementById("app")
);
