package test.com.wuxp.codegen;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeepObjectAccessor<T> {

    private final T value;

    private DeepObjectAccessor(T object) {
        this.value = object;
    }

    public static <T> DeepObjectAccessor<T> of(T object) {
        return new DeepObjectAccessor<>(object);
    }

    public <U> DeepObjectAccessor<U> accessOrDefault(Function<? super T, ? extends U> getter, U defaultVal) {
        Objects.requireNonNull(getter);
        U result = getter.apply(value);
        if (result != null) {
            return DeepObjectAccessor.of(result);
        }
        return DeepObjectAccessor.of(defaultVal);
    }

    public <U> DeepObjectAccessor<U> accessIfAbsent(Function<? super T, ? extends U> getter, BiConsumer<? super T, U> setter, U initValue) {
        Objects.requireNonNull(getter);
        Objects.requireNonNull(setter);
        U result = getter.apply(value);
        if (result != null) {
            return DeepObjectAccessor.of(result);
        }
        setter.accept(value, initValue);
        return DeepObjectAccessor.of(initValue);
    }

    public <U> DeepObjectAccessor<U> accessIfAbsent(Function<? super T, ? extends U> getter, BiConsumer<? super T, U> setter, Supplier<U> supplier) {
        return accessIfAbsent(getter, setter, supplier.get());
    }

    public <U> U getIfAbsent(Function<? super T, ? extends U> getter, BiConsumer<? super T, U> setter, Supplier<U> supplier) {
        return getIfAbsent(getter, setter, supplier.get());
    }

    public <U> U getIfAbsent(Function<? super T, ? extends U> getter, BiConsumer<? super T, U> setter, U initValue) {
        return accessIfAbsent(getter, setter, initValue).get();
    }

    public <U> U getOrDefault(Function<? super T, ? extends U> getter, U defaultVal) {
        return accessOrDefault(getter, defaultVal).get();
    }

    public T get() {
        return value;
    }
}
