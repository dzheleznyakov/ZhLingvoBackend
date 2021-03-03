package zh.lingvo.util;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Preconditions {
    private Preconditions() {
    }

    public static <E extends Exception> void checkCondition(BooleanSupplier condition, Supplier<E> exceptionSupplier) throws E {
        checkCondition(condition.getAsBoolean(), exceptionSupplier);
    }

    public static <E extends Exception> void checkCondition(boolean condition, Supplier<E> exceptionSupplier) throws E {
        if (!condition)
            throw exceptionSupplier.get();
    }

    public static <E extends Exception> void checkNull(Supplier<Object> objectSupplier, Supplier<E> exceptionSupplier) throws E {
        if (objectSupplier.get() != null)
            throw exceptionSupplier.get();
    }
}
