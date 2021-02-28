import config from "./config";
import {rewriteCooke} from "./utils";
import {defineConfig} from "umi";

console.log("use local file");

export default defineConfig({
    ...config,
    define: {
        "process.env.API_ADDRESS": "/codegen"
    },
    proxy: {
        '/codegen': {
            target: 'http://localhost:8090/codegen/',
            pathRewrite: {'^/codegen': '/'},
            changeOrigin: true,
            secure: false,
            //代理结果响应处理
            onProxyRes: function (proxyRes, req, res) {
                //重写cookie
                rewriteCooke(proxyRes, req, res)('codegen');
            }
        }
    }
})
