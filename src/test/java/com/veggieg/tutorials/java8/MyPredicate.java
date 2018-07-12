package com.veggieg.tutorials.java8;

/**
 * @author veggieg
 * @since 2018-07-12 1:03
 */
@FunctionalInterface
public interface MyPredicate<T> {
    boolean test(T t);
}
