package com.wuxp.codegen.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.*;

public final class JavaTypeUtil {

    private static final Logger logger = LoggerFactory.getLogger(JavaTypeUtil.class);

    /**
     * 是否为基础数据类型 包括String Date
     *
     * @param clazz
     * @return
     */
    public static boolean isJavaBaseType(Class<?> clazz) {
        if (clazz == null) {
            return true;
        }

        return ClassUtils.isPrimitiveOrWrapper(clazz) || isString(clazz) || isDate(clazz);
    }

    /**
     * 是否为字符串类型
     *
     * @param clazz
     * @return
     */
    public static boolean isString(Class<?> clazz) {

        if (clazz == null) {
            return false;
        }

        if (char.class.equals(clazz)) {
            return true;
        }

        if (clazz.getName().startsWith("java.lang.String")) {
            //兼容 StringBuffer StringBuilder
            return true;
        }

        return false;
    }


    /**
     * 是否为数值类型
     *
     * @param clazz
     * @return
     */
    public static boolean isNumber(Class<?> clazz) {

        if (int.class.equals(clazz) ||
                long.class.equals(clazz) ||
                float.class.equals(clazz) ||
                short.class.equals(clazz) ||
                byte.class.equals(clazz)) {
            return true;
        }
        if (Number.class.equals(clazz)) {
            return true;
        }
        return isExtendsClass(clazz, Number.class);
    }

    /**
     * 是否为时间类型
     *
     * @param clazz
     * @return
     */
    public static boolean isDate(Class<?> clazz) {
        if (Date.class.equals(clazz)) {
            return true;
        }
        return isExtendsClass(clazz, Date.class);
    }

    /**
     * 是否为boolean
     *
     * @param clazz
     * @return
     */
    public static boolean isBoolean(Class<?> clazz) {
        if (boolean.class.equals(clazz)) {
            return true;
        }
        return Boolean.class.equals(clazz);
    }

    /**
     * 是否为List 或继承自List
     *
     * @param clazz
     * @return
     */
    public static boolean isList(Class<?> clazz) {

        if (isAssignableFrom(clazz,List.class)){
            return true;
        }

        return isExtendsClass(clazz, List.class);

    }

    /**
     * 是否为Collection 或继承自 Collection
     *
     * @param clazz
     * @return
     */
    public static boolean isCollection(Class<?> clazz) {

        if (isAssignableFrom(clazz,Collection.class)){
            return true;
        }
        return isExtendsClass(clazz, Collection.class);
    }

    /**
     * 是否为Map或继承自 Map
     *
     * @param clazz
     * @return
     */
    public static boolean isMap(Class<?> clazz) {
        if (isAssignableFrom(clazz,Map.class)){
            return true;
        }
        return isExtendsClass(clazz, Map.class);
    }

    /**
     * 是否为Set或继承自 Set
     *
     * @param clazz
     * @return
     */
    public static boolean isSet(Class<?> clazz) {
        if (isAssignableFrom(clazz,Set.class)){
            return true;
        }
        return isExtendsClass(clazz, Set.class);
    }


    /**
     * 是否为 void
     *
     * @param clazz
     * @return
     */
    public static boolean isVoid(Class<?> clazz) {

        return void.class.equals(clazz);
    }

    /**
     * 是否为一个数组 例如 String [] s;
     *
     * @param clazz
     * @return
     */
    public static boolean isAnArray(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        return clazz.isArray();
    }

    /**
     * 是否为一个复杂的数据类型的数组
     *
     * @param clazz
     * @return
     */
    public static boolean isComplexAnArray(Class<?> clazz) {

        return isAnArray(clazz) && isNoneJdkComplex(clazz.getComponentType());
    }

    /**
     * 是否为非jdk中的复杂对象
     *
     * @param clazz
     * @return
     */
    public static boolean isNoneJdkComplex(Class<?> clazz) {

        if (clazz == null) {
            return false;
        }

        if (isFileUploadObject(clazz)){
            return false;
        }


        boolean isSimple = JavaTypeUtil.isJavaBaseType(clazz) ||
                JavaTypeUtil.isString(clazz) ||
                JavaTypeUtil.isBoolean(clazz) ||
                JavaTypeUtil.isDate(clazz) ||
                JavaTypeUtil.isList(clazz) ||
                JavaTypeUtil.isSet(clazz) ||
                JavaTypeUtil.isMap(clazz) ||
                JavaTypeUtil.isCollection(clazz) ||
                JavaTypeUtil.isAnArray(clazz) ||
                void.class.equals(clazz) ||
                Object.class.equals(clazz);

        if (!isSimple && clazz.getName().startsWith("java.")) {
            //不是简单的类类型，且是jdk提供的包
            return false;
        }
        return !isSimple;
    }

    /**
     * 某个类是否继承另外一个类
     *
     * @param clazz
     * @param superClazz
     * @return
     */
    public static boolean isExtendsClass(Class<?> clazz, Class<?> superClazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz.isPrimitive()) {
            //原子数据类型
            logger.warn("不支持原始数据类型：" + clazz.getSimpleName());
            return false;
        }

        if (clazz.isInterface()) {
            //接口
            Class<?>[] interfaces = clazz.getInterfaces();
            return Arrays.asList(interfaces).indexOf(superClazz) >= 0;
        }

        if (Object.class.equals(clazz)) {
            return false;
        }
        Class<?> sup = clazz.getSuperclass();

        if (superClazz.equals(sup)) {
            return true;
        }
        return isExtendsClass(sup, superClazz);
    }


    public static boolean isAssignableFrom(Class<?> clazz, Class<?> superClazz) {

        return superClazz.isAssignableFrom(clazz);
    }

    /**
     * 是否为文件上传对象
     * @param clazz
     * @return
     */
    public static boolean isFileUploadObject(Class<?> clazz){

        return CommonsMultipartFile.class.equals(clazz);
    }


}
