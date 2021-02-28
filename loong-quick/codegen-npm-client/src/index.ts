import * as log4js from "log4js";
import * as path from "path";
import * as fs from "fs";
import program from "commander";
import {downloadCodegenSdk} from "./CodegenSdkDownloader";


const logger = log4js.getLogger("codegen-cli");
logger.level = 'info';

const PROJECT_ROOT_DIR = process.cwd();//path.resolve(__dirname, '../../../')
const DEFAULT_DOWNLOAD_PATH = PROJECT_ROOT_DIR
const DEFAULT_OUTPUT_PATH = path.normalize(`${DEFAULT_DOWNLOAD_PATH}/src/api`)


/**
 * 参考文档：https://segmentfault.com/a/1190000019350684
 */
program.version("1.0.0", '-v, --version').description('用于获取服务端的sdk');


program
    .command("gen").alias("g")
    .option('-config [filepath]', '通过配置文件获取sdk')
    .action((options) => {
        let filepath = options.Config ?? options.C;
        if (filepath == null) {
            filepath = `${path.resolve(PROJECT_ROOT_DIR, 'codegenrc.json')}`;
        }
        logger.info(`配置文件所在路径：${filepath}`);
        const config: string = fs.readFileSync(filepath, {encoding: "UTF-8", flag: "r"});
        const downloadConfig = {
            downloadPath: DEFAULT_DOWNLOAD_PATH,
            output: DEFAULT_OUTPUT_PATH,
            ...new Function(`return ${config}`)()
        };
        if (downloadConfig.output !== DEFAULT_OUTPUT_PATH) {
            // 根据项目根目录的src目录计算
            downloadConfig.output = path.resolve(PROJECT_ROOT_DIR, "src", downloadConfig.output);
        }
        downloadCodegenSdk(downloadConfig);
    })
    .parse(process.argv);

program.on('--help', () => {
    console.log('这是帮助信息:')
    console.log('codegen gen -config [filepath]  filepath相对于项目根路径')
    console.log('-gen [filepath')
});