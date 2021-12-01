import {ProxyConfigMap} from "webpack-dev-server";


export const httpProxyConfig: ProxyConfigMap = {

    "/codegen": {
        target: "http://localhost:8080/codegen",
        changeOrigin: true,
        pathRewrite: {
            "^/codegen": `/`
        },
        onProxyReq(proxyReq, req) {
            const origin = `${req.protocol}://${req.hostname}:3000`;
            proxyReq.setHeader("origin", origin);
        },
    },
}
