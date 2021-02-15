const execSync = require('child_process').execSync;

let args = process.argv.splice(2);
let filepath = args[0];
let encoding = args[1] || "UTF-8";

// 调用命名行格式化代码
let formatCmd = "prettier --write " + filepath;
execSync(formatCmd, {stdio: 'inherit'});