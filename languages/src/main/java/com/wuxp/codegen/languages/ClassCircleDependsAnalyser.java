package com.wuxp.codegen.languages;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxp
 */
public class ClassCircleDependsAnalyser {

    private final List<Class<?>> analysisPaths;

    public ClassCircleDependsAnalyser() {
        this.analysisPaths = new ArrayList<>();
    }

    public boolean joinNode(Class<?> clazz) {
        if (isCircle(clazz)) {
            return false;
        }
        analysisPaths.add(clazz);
        return true;
    }

    public void removeLastNode(Class<?> clazz) {
        Class<?> removeNode = analysisPaths.remove(analysisPaths.size() - 1);
        Assert.isTrue(removeNode == clazz, "移除 lastNode 失败：" + clazz.getName());
    }


    private boolean isCircle(Class<?> clazz) {
        return analysisPaths.contains(clazz);
    }
}
