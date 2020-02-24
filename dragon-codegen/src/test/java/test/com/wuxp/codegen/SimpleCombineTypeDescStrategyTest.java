package test.com.wuxp.codegen;

import com.wuxp.codegen.dragon.strategy.SimpleCombineTypeDescStrategy;
import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SimpleCombineTypeDescStrategyTest {

    @Test
    public void combine() {

        SimpleCombineTypeDescStrategy strategy = new SimpleCombineTypeDescStrategy();
//        List<String> list0 = GrabGenericVariablesHelper.matchGenericDescriptors("Map<Promise<T>,String>");
//        System.out.println(list0);
//        List<String> list1 = GrabGenericVariablesHelper.matchGenericDescriptors("List<Map<K,PageInfo<T>>>");
//        System.out.println(list1);
//        List<String> list2 = GrabGenericVariablesHelper.matchGenericDescriptors("Promise<Map<K,V>>");
//        System.out.println(list2);
//
        List<String> names=new ArrayList<>();
//        names.add("Map<K,V>");
//        names.add("Promise<T>");
//        names.add("String");
//        names.add("User");
//        System.out.println(strategy.combineTypes(names));
//        names.clear();
//        names.add("Record<K,V>");
//        names.add("string");
//        names.add("Array<T>");
//        names.add("Array<T>");
//        names.add("string");
//        System.out.println(strategy.combineTypes(names));
//        names.clear();


        names.add("Promise<K>");
        names.add("Map<K,V>");
        names.add("PageInfo<T>");
        names.add("User");
        names.add("List<T>");
        names.add("Page<T>");
        names.add("Member");



        System.out.println(strategy.combineTypes(names));
    }

    public static void main(String[] args) {
        System.out.println(File.separator);
    }
}
