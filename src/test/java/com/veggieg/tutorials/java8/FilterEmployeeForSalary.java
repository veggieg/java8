package com.veggieg.tutorials.java8;

/**
 * @author veggieg
 * @since 2018-07-12 1:06
 */
public class FilterEmployeeForSalary implements MyPredicate<Employee> {
    @Override
    public boolean test(Employee t) {
        return t.getSalary() >= 5000;
    }
}
