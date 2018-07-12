package com.veggieg.tutorials.java8;

/**
 * @author veggieg
 * @since 2018-07-12 1:06
 */
public class FilterEmployeeForAge implements MyPredicate<Employee> {

    @Override
    public boolean test(Employee t) {
        return t.getAge() <= 35;
    }
}