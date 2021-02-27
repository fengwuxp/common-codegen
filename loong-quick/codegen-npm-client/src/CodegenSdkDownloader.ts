import download from "download";
import extract from "extract-zip";
import {stringify} from "querystring";
import * as path from "path";
import * as log4js from "log4js";
import * as fsExtra from "fs-extra";

const logger = log4js.getLogger("codegen-client");
logger.level = 'info';

export interface DownloadCodegenSdkOptions {

    loongCodegenServer: string;

    projectName: string;

    branch: string;

    moduleName: string;

    type: "spring_cloud_openfeign" | "retrofit" | "dart_feign" | "typescript_feign" | "umi_request";

    /**
     * 代码下载的目录
     */
    downloadPath: string;

    /**
     * 代码输出目录
     */
    output?: string;
}

const DEFAULT_OPTIONS: Partial<DownloadCodegenSdkOptions> = {
    type: "typescript_feign",
    moduleName: "web"
}

const downloadSdk = async (loongCodegenServer: string, projectName: string, type: string, moduleName: string, downloadPath: string): Promise<void> => {
    const url = `${loongCodegenServer}/codegen/loong/sdk_code?${stringify({projectName, type, moduleName})}`;
    logger.info(`下载${projectName}的sdk${url}`)
    await download(url, downloadPath);
}

/**
 * 下载sdk，并解压到对应的目录
 * @param options
 */
export const downloadCodegenSdk = async (options: DownloadCodegenSdkOptions): Promise<void> => {

    const {loongCodegenServer, projectName, type, moduleName, downloadPath, output} = {...DEFAULT_OPTIONS, ...options};
    // 下载sdk
    await downloadSdk(loongCodegenServer, projectName, type, moduleName, downloadPath);

    // unzip
    const sdkFilepath = path.normalize([downloadPath, type.toLowerCase()].join("/")) + ".zip"
    const targetDir = output ?? downloadPath;
    await extract(sdkFilepath, {dir: targetDir});
    const srcDir = path.normalize([targetDir, type.toLowerCase(), "src"].join("/"))
    // 复制到期望的目录下
    fsExtra.copySync(srcDir, targetDir);
    // 删除下载的zip文件
    fsExtra.removeSync(sdkFilepath);
    // 删除解压后的文件
    fsExtra.removeSync(path.normalize([targetDir, type.toLowerCase()].join("/")));
}
