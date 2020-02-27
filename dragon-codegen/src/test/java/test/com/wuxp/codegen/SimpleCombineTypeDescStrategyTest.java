package test.com.wuxp.codegen;

import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SimpleCombineTypeDescStrategyTest {

    @Test
    public void combine() {

        SimpleCombineTypeDescStrategy strategy = new SimpleCombineTypeDescStrategy();
        List<String> list0 = GrabGenericVariablesHelper.matchGenericDescriptors("Map<Promise<T>,String>");
        System.out.println(list0);
        List<String> list1 = GrabGenericVariablesHelper.matchGenericDescriptors("List<Map<K,PageInfo<T>>>");
        System.out.println(list1);
        List<String> list2 = GrabGenericVariablesHelper.matchGenericDescriptors("Promise<Map<K,V>>");
        System.out.println(list2);

        List<String> names = new ArrayList<>();
        names.add("Map<K,V>");
        names.add("Promise<T>");
        names.add("String");
        names.add("User");
        Assert.assertEquals("合并失败", "Map<Promise<String>,User>", strategy.combineTypes(names));
        names.clear();
        names.add("Record<K,V>");
        names.add("string");
        names.add("Array<T>");
        names.add("Array<T>");
        names.add("string");
        Assert.assertEquals("合并失败", "Record<string,Array<Array<string>>>", strategy.combineTypes(names));
        names.clear();
        names.add("Promise<K>");
        names.add("Map<K,V>");
        names.add("PageInfo<T>");
        names.add("User");
        names.add("List<T>");
        names.add("Page<T>");
        names.add("Member");


        Assert.assertEquals("合并失败", "Promise<Map<PageInfo<User>,List<Page<Member>>>>", strategy.combineTypes(names));
    }

    public static void main(String[] args) {
        System.out.println(File.separator);
    }
}
