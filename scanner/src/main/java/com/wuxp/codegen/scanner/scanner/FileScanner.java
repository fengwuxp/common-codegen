package com.wuxp.codegen.scanner.scanner;

import com.wuxp.codegen.scanner.util.FileStringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过文件扫描包
 */
public class FileScanner implements Scanner<List<String>, String> {


    private String fileExtension = ".java";


    @Override
    public List<String> scan(String fileName) {
        List<String> filePaths = new ArrayList<>();
        String projectFolderName = getProjectFolder();
        getFileNames(projectFolderName, fileName, filePaths);
        return filePaths;
    }


    private void getFileNames(String folderName, String baseFileName, List<String> filePaths) {
        File folder = new File(folderName);
        File[] content = folder.listFiles();

        if (content != null) {
            for (File file : content) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (FileStringUtils.getFileExtension(fileName).equalsIgnoreCase(fileExtension)
                            && FileStringUtils.getFileName(fileName).contains(baseFileName)) {
                        filePaths.add(file.getPath());
                    }
                } else {
                    getFileNames(file.getPath(), baseFileName, filePaths);
                }
            }
        }
    }

    private String getProjectFolder() {
        String[] classPath = FileStringUtils.getClassPathElements();
        String defaultFolder = "";
        for (String fileName : classPath) {
            if (!FileStringUtils.isJar(fileName)) {
                defaultFolder = fileName;
            }
        }

        return defaultFolder;
    }
}


