import download from "download";
import {stringify} from "querystring";

export interface DownloadCodegenSdkOptions {

    loongCodegenServer: string;

    projectName: string;

    branch: string;

    moduleName: string;

    type: string;

    output: string;

}

/**
 * 下载sdk
 * @param options
 */
export const downloadCodegenSdk = async (options: DownloadCodegenSdkOptions): Promise<void> => {
    const {loongCodegenServer, projectName, type, moduleName, output} = options;
    const url = `${loongCodegenServer}/codegen/loong/sdk_code?${stringify({projectName, type, moduleName})}`;
    await download(url, output);
}

