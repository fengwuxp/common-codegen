import codegen from "./codegenrc.json";
import {downloadCodegenSdk} from "../src/CodegenSdkDownloader";
import * as fsExtra from "fs-extra";
import * as log4js from "log4js";

const logger = log4js.getLogger();
logger.level = 'debug';

describe("test codegen", () => {

    test("test codegen download", async () => {
        const downloadPath = `${__dirname}/temp`;
        await downloadCodegenSdk({
            ...(codegen as any),
            downloadPath,
            output: `${downloadPath}/src/feign`
        });
        fsExtra.removeSync(downloadPath);
    })



});
