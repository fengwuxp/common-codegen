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
 *
 * @author wuxp
 * @see com.wuxp.codegen.core.util.PathResolveUtils
 */
@Slf4j
public class PathResolve {


    /**
     * Right slash
     */
    public static final String RIGHT_SLASH = "/";

    private static final String DOT = ".";

    /**
     * 用于判断是否为文件的一个正则表达式
     */
    private static final String FILE_PATH_REGEXP = "\\w*.[a-zA-Z]{2,5}$";


    /**
     * 计算2个路径的相对路径解析
     * <p>
     * /api/models/b
     * /api/clients/a
     * ==>
     * ../models/b
     * </p>
     *
     * @param baseDir 基础路径
     * @param args    目前只支持2个参数
     * @return 解析后的路径
     */
    public String relativizeResolve(String baseDir, String... args) {
        if (args == null || args.length <= 1) {
            log.error("根路径{}，需要导入依赖的路径描述有误 {}", baseDir, args);
            return "";
        }

        Path basePath = Paths.get(baseDir);
        List<Path> paths = Arrays.stream(args)
                .filter(StringUtils::hasText)
                .map(path -> basePath.resolveSibling(Paths.get(path)))
                .collect(Collectors.toList());
        if (paths.isEmpty()) {
            log.error("根路径{}，需要导入依赖的文件 {}，导入的依赖路径  {}", baseDir, args[0], args[1]);
            return null;
        }
        String pathArgs2 = paths.get(1).toString();
        if (!pathArgs2.startsWith(DOT) && !pathArgs2.startsWith(File.separator)) {
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

        if (!result.startsWith(DOT)) {
            //转换为相对路径
            result = String.join("", DOT, RIGHT_SLASH, result);
        }
        return result;
    }

    /**
     * 是一个文件路径还是文件夹路径
     *
     * @param path 路径
     * @return 是否为文件
     */
    private boolean isFile(String path) {
        Pattern pattern = Pattern.compile(FILE_PATH_REGEXP);
        Matcher matcher = pattern.matcher(path);
        return matcher.find();

    }

}
