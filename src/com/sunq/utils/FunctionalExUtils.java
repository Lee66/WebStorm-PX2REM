package com.sunq.utils;

import sun.rmi.runtime.Log;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sunqian
 * @date 2019/3/22
 */
public class FunctionalExUtils {

    private volatile static FunctionalExUtils functionalExUtils;

    private LogicUtils logic;
    private CollectionUtils collections;

    public FunctionalExUtils() {
        this.logic = LogicUtils.getLogic();
        this.collections = CollectionUtils.getCollections();
    }

    public static FunctionalExUtils getFunctionalEx() {
        if (functionalExUtils == null) {
            synchronized (FunctionalExUtils.class) {
                if (functionalExUtils == null) {
                    functionalExUtils = new FunctionalExUtils();
                }
            }
        }
        return functionalExUtils;
    }

    /**
     * Function类型的异常捕捉
     *
     * @param mapper
     * @param exceptionClass
     * @param <T>
     * @param <R>
     * @param <E>
     * @return
     */
    public <T, R, E extends Exception> Function<T, R> functionWrapper(UncheckedFunction<T, R> mapper, Class<E>... exceptionClass) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                return mapper.apply(t);
            } catch (RuntimeException ex) {
                return this.logic.funOrElse(exceptionClass, Objects::nonNull, excep ->
                        this.collections.getFirst(Arrays.asList(excep).stream().filter(exception -> Objects.equals(ex.getClass(), exception)).collect(Collectors.toList()), e -> {
                            try {
                                throw e.cast(ex);
                            } catch (Exception e1) {
                                throw new RuntimeException(e1);
                            }
                        }), exception -> {
                    throw ex;
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public <T, R, E extends Exception> Function<T, R> functionWrapper(UncheckedFunction<T, R> mapper) {
        return this.functionWrapper(mapper, null);
    }

    /**
     * Consumer类型的异常捕捉
     *
     * @param throwingConsumer
     * @param exceptionClass
     * @param <T>
     * @param <E>
     * @return
     */
    public <T, E extends Exception> Consumer<T> consumerWrapper(ThrowingConsumer<T, E> throwingConsumer, Class<E>... exceptionClass) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (RuntimeException ex) {
                this.logic.conOrElse(exceptionClass, Objects::nonNull, excep ->
                        this.collections.getFirst(Arrays.asList(excep).stream().filter(exception -> Objects.equals(ex.getClass(), exception)).collect(Collectors.toList()), e -> {
                            try {
                                throw e.cast(ex);
                            } catch (Exception e1) {
                                throw new RuntimeException(e1);
                            }
                        }), exception -> {
                    throw ex;
                });
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public <T, E extends Exception> Consumer<T> consumerWrapper(ThrowingConsumer<T, E> throwingConsumer) {
        return this.consumerWrapper(throwingConsumer, null);
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface UncheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }

}
