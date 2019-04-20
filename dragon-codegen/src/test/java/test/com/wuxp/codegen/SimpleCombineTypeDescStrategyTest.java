package test.com.wuxp.codegen;

import com.wuxp.codegen.dragon.strategy.SimpleCombineTypeDescStrategy;
import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleCombineTypeDescStrategyTest {

    @Test
    public void combine() {

        SimpleCombineTypeDescStrategy strategy = new SimpleCombineTypeDescStrategy();
        List<String> list1 = GrabGenericVariablesHelper.matchGenericDescriptors("List<Map<K,PageInfo<T>>>");
        System.out.println(list1);
        List<String> list2 = GrabGenericVariablesHelper.matchGenericDescriptors("Promise<Map<K,V>>");
        System.out.println(list2);

        List<String> names=new ArrayList<>();
        names.add("Promise<T>");
        names.add("Map<K,V>");
        names.add("String");
        names.add("User");
        System.out.println(strategy.combineTypes(names));
    }

    public static void main(String[] args) {
        System.out.println(File.separator);
    }
}