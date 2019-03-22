package com.sunq.utils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author sunqian
 * @date 2019/3/22
 */
public class LogicUtils {

    private volatile static LogicUtils logicUtils;

    private LogicUtils() {

    }

    public static LogicUtils getLogic() {
        if (logicUtils == null) {
            synchronized (LogicUtils.class) {
                if (logicUtils == null) {
                    logicUtils = new LogicUtils();
                }
            }
        }
        return logicUtils;
    }

    public <T> void conOrElse(T t, Predicate<T> predicate, Consumer<T> consumerTrue, Consumer<T> consumerFalse) {
        if (predicate.test(t)) {
            consumerTrue.accept(t);
        } else {
            consumerFalse.accept(t);
        }
    }

    public <T, R> R funOrElse(T t, Predicate<T> predicate, Function<T, R> functionTrue, Function<T, R> functionFalse) {
        if (predicate.test(t)) {
            return functionTrue.apply(t);
        } else {
            return functionFalse.apply(t);
        }
    }

    public <T> void conOrThrow(T t, Predicate<T> predicate, Consumer<T> consumer, RuntimeException e) {
        if (predicate.test(t)) {
            consumer.accept(t);
        } else {
            throw e;
        }
    }

    public <T, R> R funOrThrow(T t, Predicate<T> predicate, Function<T, R> function, RuntimeException e) {
        if (predicate.test(t)) {
            return function.apply(t);
        } else {
            throw e;
        }
    }

    public <T> void conOrEnd(T t, Predicate<T> predicate, Consumer<T> consumer) {
        if (predicate.test(t)) {
            consumer.accept(t);
        }
    }

    public <T, R> R funOrEnd(T t, Predicate<T> predicate, Function<T, R> function, R end) {
        if (predicate.test(t)) {
            return function.apply(t);
        }
        return end;
    }

    public <T,R> R funOrEndWithEx(T t, Predicate<T> predicate, Function<T, R> funcTrue, Function<T, R> funcFalse, R end){
        try{
            if(predicate.test(t)){
                return funcTrue.apply(t);
            } else {
                return funcFalse.apply(t);
            }
        } catch (Exception e){
            return end;
        }
    }

    public <T> void funWithWhile(T t, Predicate<T> predicate, Function<T, T> function){
        while(predicate.test(t)){
            t = function.apply(t);
        }
    }

}
