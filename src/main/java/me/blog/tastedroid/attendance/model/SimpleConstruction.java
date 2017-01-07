package me.blog.tastedroid.attendance.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class SimpleConstruction<T> implements Construction {

    private final Constructor<T> constructor;
    private final Supplier<Object[]> arguments;

    public SimpleConstruction(Constructor<T> constructor, Supplier<Object[]> arguments) {
        this.constructor = constructor;
        this.arguments = arguments;
    }

    public SimpleConstruction(Constructor<T> constructor) {
        this(constructor, () -> new Object[0]);
    }

    @Override
    public T construct() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ExceptionInInitializerError {
        return constructor.newInstance(arguments.get());
    }
}
