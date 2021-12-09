import React from "react";
import * as ReactDOM from "react-dom";
import {I18nextProvider} from 'react-i18next';
import BrowserFeignConfigurer, {registerHttpResponseEventListener} from "@/BrowserFeignConfigurer";
import App from "@/App";
import {Spin} from "antd";
import {feignConfigurationInitialize} from "feign-boot-starter";
import {setDefaultLoadingComponent} from "@/components/loading/AsyncLoading";
import i18n from '@/i18n';

// 设置默认组件加载 loading
setDefaultLoadingComponent(Spin);

//  注册feign 代理
const feignConfig = feignConfigurationInitialize(new BrowserFeignConfigurer());
registerHttpResponseEventListener(feignConfig.getHttpResponseEventListener())

// Render the top-level React component
ReactDOM.render(
    <I18nextProvider i18n={i18n}>
        <App/>
    </I18nextProvider>,
    document.getElementById("app")
);
