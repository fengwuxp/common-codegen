/**
 * 配置环境变量
 */
export default {
    dev: {
        API_ENTRY_ADDRESS: "/codegen",
        DEFAULT_LANGUAGE: "",
        GOOGLE_ANALYTICS_UA: ""
    },
    test: {
        API_ENTRY_ADDRESS: "https://test.exmaple.com/api"
    },
    prod: {
        API_ENTRY_ADDRESS: "https://prod.exmaple.com/api"
    }
}
