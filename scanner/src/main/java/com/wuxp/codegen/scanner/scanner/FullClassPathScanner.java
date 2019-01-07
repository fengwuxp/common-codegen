package com.wuxp.codegen.scanner.scanner;

import com.wuxp.codegen.scanner.util.FileStringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 按照类路径扫描包
 */
@Slf4j
public class FullClassPathScanner implements Scanner<Set<String>, String> {


//    public List<String> scan(String packageName) {
//
//        String filePath = packageName.replace('.', '/');
//        try {
//            if (FileStringUtils.isJar(filePath)) {
//                JarFile jarFile = new JarFile(filePath);
//                return getClassNames(jarFile.entries());
//            } else {
//                Enumeration<URL> dirs = null;
//                try {
//                    dirs = Thread.currentThread().getContextClassLoader().getResources(packageName);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (dirs == null) {
//                    return Collections.EMPTY_LIST;
//                }
//
//                while (dirs.hasMoreElements()) {
//                    URL url = dirs.nextElement();
//                    filePath = URLDecoder.decode(url.getFile(), "UTF-8");
//                }
//
//                return getClassNames(filePath, packageName, new ArrayList<>());
//            }
//        } catch (Exception exception) {
//            if (log.isDebugEnabled()) {
//                log.error("Can't read file: {}", filePath, exception);
//            }
//        }
//        return Collections.EMPTY_LIST;
//
//    }

    /**
     * 扫描获取所有的类路径
     *
     * @param packageName
     * @return 该包下的所有类的类路径
     */
    @Override
    public Set<String> scan(String packageName) {
        if (packageName == null) {
            return Collections.EMPTY_SET;
        }
        try {
            return fetchClassNames(packageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_SET;
    }

    private Set<String> fetchClassNames(String packageName) throws IOException {
        //第一个class类的集合
        Set<String> classes = new HashSet<String>();


        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

        //循环迭代下去
        while (dirs.hasMoreElements()) {
            //获取下一个元素
            URL url = dirs.nextElement();

            //得到协议的名称
            String protocol = url.getProtocol();

            //如果是以文件的形式保存在服务器上
            if ("file".equals(protocol)) {
                //获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                this.getClassNames(filePath, packageName, classes);
            } else if ("jar".equals(protocol)) {
                //jar
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                //从此jar包 得到一个枚举类
                Enumeration<JarEntry> entries = jar.entries();
                return this.getClassNames(entries);
            } else {
                //TODO

            }
        }
        return classes;
    }


    private Set<String> getClassNames(Enumeration<JarEntry> entities) {
        Set<String> classNames = new HashSet<>();

        while (entities.hasMoreElements()) {
            String entityName = entities.nextElement().toString();
            if (FileStringUtils.isClass(entityName)) {
                classNames.add(FileStringUtils.normalizeClassName(entityName));
            }
        }

        return classNames;
    }

    private Set<String> getClassNames(String classPath, String classFolder, Set<String> classNames) {

        File folder = new File(classPath);

        File[] content = folder.listFiles();

        if (content == null) {
            return classNames;
        }
        for (File file : content) {
            String fileName = file.getName();
            if (file.isFile() && FileStringUtils.isClass(fileName)) {
//                String fileName = FileStringUtils.substringPath(file.getPath(), classFolder);
//                classNames.add(FileStringUtils.normalizeClassName(fileName));
                //获取className 例如 test.com.wuxp.A
                String className = fileName.substring(0, file.getName().length() - 6);
                className = classFolder + '.' + className;
                classNames.add(className);
            } else {
                getClassNames(file.getPath(), classFolder, classNames);
            }
        }

        return classNames;

    }
}
