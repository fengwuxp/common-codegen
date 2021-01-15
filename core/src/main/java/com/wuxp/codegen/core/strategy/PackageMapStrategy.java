package com.wuxp.codegen.core.strategy;

/**
 * 包名映射策略
 *
 * @author wuxp
 */
public interface PackageMapStrategy {


    /**
     * 将一个类转换为期望的输出路径（基于输出根路径）
     *
     * @param clazz 参与生成的类
     * @return 导入该类的导入语句字符串
     */
    String convert(Class<?> clazz);


    /**
     * 转换 class Name
     *
     * @param clazz 参与生成的类
     * @return 转换后的类名
     */
    String convertClassName(Class<?> clazz);


    /**
     * 生成包名路径
     *
     * @param uris 路径列表
     * @return 合并后的路径
     */
    String genPackagePath(String[] uris);
}
