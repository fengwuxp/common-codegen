package com.wuxp.codegen.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public final class JavaTypeUtil {

    private static final Logger logger = LoggerFactory.getLogger(JavaTypeUtil.class);

    /**
     * 是否为基础数据类型 包括String Date
     *
     * @param clazz
     * @return
     */
    public static boolean isJavaBaseType(Class<?> clazz) {


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

        if (clazz.getName().startsWith("java.lang.String")) {
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
     * 是否为一个简单数据类型的数组
     *
     * @param clazz
     * @return
     */
    public static boolean isComplexAnArray(Class<?> clazz) {

        return isAnArray(clazz) && isComplex(clazz.getComponentType());
    }

    /**
     * 是否为复杂对象
     *
     * @param clazz
     * @return
     */
    public static boolean isComplex(Class<?> clazz) {


        boolean b = JavaTypeUtil.isJavaBaseType(clazz) ||
                JavaTypeUtil.isString(clazz) ||
                JavaTypeUtil.isBoolean(clazz) ||
                JavaTypeUtil.isDate(clazz) ||
                void.class.equals(clazz);
        return !b;
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



    public static boolean isCollection(Class<?> clazz) {


        return isExtendsClass(clazz, Collection.class);

    }


    public static void main(String[] args) {
//        System.out.println(isJavaBaseType(int.class));
//        System.out.println(isJavaBaseType(Integer.class));
//        System.out.println(isJavaBaseType("".getClass()));
//        System.out.println(isString("".getClass()));
        System.out.println(isExtendsClass(Integer.class, Object.class));
//        System.out.println(isExtendsClass(ServiceEvt.class, Object.class));
    }


}
