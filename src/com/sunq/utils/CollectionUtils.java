package com.sunq.utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author sunqian
 * @date 2019/3/22
 */
public class CollectionUtils {

    private volatile static CollectionUtils collectionUtils;

    public CollectionUtils() {
    }

    public static CollectionUtils getCollections() {
        if (collectionUtils == null) {
            synchronized (CollectionUtils.class) {
                if (collectionUtils == null) {
                    collectionUtils = new CollectionUtils();
                }
            }
        }
        return collectionUtils;
    }

    public <E, R> R getFirst(List<E> list, Function<E, R> function) {
        if (Objects.nonNull(list) && list.size() > 0) {
            return function.apply(list.get(0));
        } else {
            throw null;
        }
    }

}
