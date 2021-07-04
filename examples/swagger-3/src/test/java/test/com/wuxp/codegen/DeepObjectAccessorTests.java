package test.com.wuxp.codegen;

import com.wuxp.codegen.swagger3.example.maven.domain.Order;
import com.wuxp.codegen.swagger3.example.maven.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DeepObjectAccessorTests {


    @Test
    void test() {
        Integer age = DeepObjectAccessor.of(new Order())
                .accessIfAbsent(Order::getUser, Order::setUser, new User())
                .getOrDefault(User::getAge, 100);
        Assertions.assertEquals(100, age);

        User user = DeepObjectAccessor.of(new Order()).getIfAbsent(Order::getUser, Order::setUser, new User());
        Assertions.assertNotNull(user);

//        DeepObjectAccessor.of(new Order())
//                .access(Order::getUser)
//                .orNullable(Order::setUser,new User())
    }
}
