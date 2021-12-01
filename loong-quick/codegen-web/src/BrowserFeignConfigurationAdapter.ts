import {
    ClientHttpRequestInterceptorFunction,
    CodecFeignClientExecutorInterceptor,
    DateEncoder,
    HttpErrorResponseEventPublisherExecutorInterceptor,
    HttpMediaType,
    HttpResponse,
    HttpStatus,
    NetworkClientHttpRequestInterceptor,
    RoutingClientHttpRequestInterceptor,
    SimpleNetworkStatusListener,
    stringDateConverter
} from 'fengwuxp-typescript-feign'
import {
    ClientHttpInterceptorRegistry,
    FeignClientInterceptorRegistry,
    FeignConfigurationAdapter,
} from 'feign-boot-starter'
import {BrowserHttpAdapter, BrowserNetworkStatusListener} from 'feign-boot-browser-starter'
import {message} from 'antd';

import {API_ENTRY_ADDRESS, LANGUAGE_HEADER_NAME} from "@/env/EvnVariable";
import {AppStorage} from "@/AppStorage";

const I18nClientHttpRequestInterceptor: ClientHttpRequestInterceptorFunction = (req) => {
    req.headers[LANGUAGE_HEADER_NAME] = AppStorage.getLocaleNameSync();
    return Promise.resolve(req);
}

export default class BrowserFeignConfigurationAdapter implements FeignConfigurationAdapter {

    public defaultProduce = () => HttpMediaType.APPLICATION_JSON;

    public httpAdapter = () => new BrowserHttpAdapter(20 * 1000);

    public registryClientHttpRequestInterceptors = (interceptorRegistry: ClientHttpInterceptorRegistry) => {
        interceptorRegistry.addInterceptor(new NetworkClientHttpRequestInterceptor(
            new BrowserNetworkStatusListener(),
            new SimpleNetworkStatusListener()));
        interceptorRegistry.addInterceptor(new RoutingClientHttpRequestInterceptor(API_ENTRY_ADDRESS));
        interceptorRegistry.addInterceptor(I18nClientHttpRequestInterceptor);
    };

    public registryFeignClientExecutorInterceptors = (interceptorRegistry: FeignClientInterceptorRegistry) => {

        // codec
        interceptorRegistry.addInterceptor(new CodecFeignClientExecutorInterceptor(
            [
                // 将时间类型转换为 时间戳格式
                new DateEncoder(stringDateConverter()),
            ],
        ));

        const unifiedFailureToast = (response: HttpResponse | string) => {
            console.log('======response====>', response);
            message.error(this.getRespErrorMessage(response) ?? "系统错误", 1.5);
        };
        // 统一错误提示
        interceptorRegistry.addInterceptor(new HttpErrorResponseEventPublisherExecutorInterceptor(unifiedFailureToast))
    };

    private getRespErrorMessage = (response: HttpResponse<any> | string): string => {
        if (typeof response === "object") {
            if (response.statusCode === HttpStatus.GATEWAY_TIMEOUT) {
                return response.statusText;
            } else {
                const data = response.data;
                if (data != null) {
                    return data.message;
                }
            }
        }
        return response as string;
    }


    public feignUIToast = () => (text: string) => {
        message.error(text || "系统错误")
    };
}


