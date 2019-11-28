package com.wuxp.codegen.dragon.path;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 路径解析
 */
@Slf4j
public class PathResolve {


    /**
     * Right slash
     */
    public static final String RIGHT_SLASH = "/";


    /**
     * 相对路径解析
     *
     * @param baseDir
     * @param args    目前只支持2个参数
     * @return
     */
    public String relativizeResolve(String baseDir, String... args) {
        if (args == null || args.length <= 1) {
//            return null;
            log.error("根路径{}，需要导入依赖的路径描述有误 {}", baseDir, args);
            return "";
        }

        Path basePath = Paths.get(baseDir);

        List<Path> paths = Arrays.stream(args)
                .filter(StringUtils::hasText)
                .map(path -> basePath.resolveSibling(Paths.get(path)))
                .collect(Collectors.toList());
        if (paths.size() == 0) {
            log.error("根路径{}，需要导入依赖的文件 {}，导入的依赖路径  {}", baseDir, args[0], args[1]);
            return null;
        }
        String pathArgs2 = paths.get(1).toString();
        if (!pathArgs2.startsWith(".") && !pathArgs2.startsWith(File.separator)) {
            return args[1];
        }
        Path p = paths.get(0);
        for (int i = 1; i < paths.size(); i++) {
            p = p.relativize(paths.get(i));
        }
        String result = p.normalize().toString();

        if (this.isFile(paths.get(paths.size() - 1).toString())) {
            //如果最后一个路径是文件，裁剪掉第一个..\
            if (result.trim().length() > 3) {
                result = result.substring(3);
            } else {
                log.warn("相对路径的解析结果小于3 ，path result = {}", result);
            }

        }
        //转换导入的路径 将 \A\b-> /A/b
        result = result.replaceAll(String.format("\\%s", File.separator), RIGHT_SLASH);

        if (!result.startsWith(".")) {
            //转换为相对路径
            result = String.join("", ".", RIGHT_SLASH, result);
        }
        return result;
    }

    /**
     * 是一个文件路径还是文件夹路径
     *
     * @param path
     * @return
     */
    private boolean isFile(String path) {

        String regexp = "\\w*.[a-zA-Z]{2,5}$";

        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(path);
        return matcher.find();

    }


    public static void main(String[] args) {


//        Path path1 = Paths.get("/api/services/member/A.ts");
//        System.out.println(path1.getRoot());
//        Path path2 = path1.relativize(Paths.get("/api/req/member/B.ts"));
//        Path api = Paths.get("api");
//        Path path1 = api.resolveSibling(Paths.get("services", "member"));
//        Path path2 = api.resolveSibling(Paths.get("req", "member"));
////        System.out.println( path1.resolve(path2));; //folder1\sub1\folder2\sub2
////        System.out.println(path1.resolveSibling(path2));; //folder1\folder2\sub2
//        System.out.println(path1.relativize(path2).normalize());
//        ; //..\..\folder2\sub2
//        System.out.println(path1.subpath(0, 1)); //folder1
//
//        System.out.println(path2);

        PathResolve pathResolve = new PathResolve();
        String path = pathResolve.relativizeResolve("api",
                "/services/member/A.ts",
                "/req/member/B.ts");
        System.out.println(path);
    }
}
