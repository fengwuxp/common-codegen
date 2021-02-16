const childProcess = require('child_process');
const fs = require('fs');

let args = process.argv.splice(2);
let filepath = args[0];
let encoding = args[1] || "UTF-8";

/**
 * npm 获取全局安装包路径 https://blog.csdn.net/ljy_1024/article/details/103610443
 * @return {*}
 */
const getGlobalModulesPath = () => {
    const result = childProcess.execSync("npm root -g", {
        encoding: "utf-8"
    });
    return result.replace("\n", "").replace("\r", "");
}

const formatSourceFile = (filepath, encoding) => {
    const prettierModule = getGlobalModulesPath() + "/prettier/index.js";
    const prettier = require(prettierModule);
    const sourcecode = fs.readFileSync(filepath, {encoding: encoding});
    const result = prettier.format(sourcecode, {semi: false, parser: "typescript"});
    fs.writeFileSync(filepath, result, {
        encoding: encoding
    });
}
formatSourceFile(filepath, encoding);
