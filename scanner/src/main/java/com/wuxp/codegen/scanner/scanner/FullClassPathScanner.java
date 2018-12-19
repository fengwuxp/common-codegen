package com.wuxp.codegen.scanner.scanner;

import com.wuxp.codegen.scanner.util.FileStringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 按照类路径扫描包
 */
@Slf4j
public class FullClassPathScanner implements Scanner<List<String>, String> {


    /**
     * 扫描获取所有的类路径
     *
     * @param packageName
     * @return 该包下的所有类的类路径
     */
    @Override
    public List<String> scan(String packageName) {

        String filePath = packageName.replace('.', '/');
        try {
            if (FileStringUtils.isJar(filePath)) {
                JarFile jarFile = new JarFile(filePath);
                return getClassNames(jarFile.entries());
            } else {
                return getClassNames(filePath, packageName, new ArrayList<>());
            }
        } catch (Exception exception) {
            if (log.isDebugEnabled()) {
                log.error("Can't read file: {}", filePath, exception);
            }
        }
        return new ArrayList<String>();

    }


    private List<String> getClassNames(Enumeration<JarEntry> entities) {
        List<String> classNames = new ArrayList<>();

        while (entities.hasMoreElements()) {
            String entityName = entities.nextElement().toString();
            if (FileStringUtils.isClass(entityName)) {
                classNames.add(FileStringUtils.normalizeClassName(entityName));
            }
        }

        return classNames;
    }

    private List<String> getClassNames(String classPath, String classFolder, List<String> classNames) {
        File folder = new File(classPath);
        File[] content = folder.listFiles();

        if (content != null) {
            for (File file : content) {
                if (file.isFile() && FileStringUtils.isClass(file.getName())) {
                    String fileName = FileStringUtils.substringPath(file.getPath(), classFolder);
                    classNames.add(FileStringUtils.normalizeClassName(fileName));
                } else {
                    getClassNames(file.getPath(), classFolder, classNames);
                }
            }
        }

        return classNames;
    }
}
