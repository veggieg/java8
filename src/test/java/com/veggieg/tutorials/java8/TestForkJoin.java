package com.veggieg.tutorials.java8;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class TestForkJoin {
    /**
     * fork - join
     */
    @Test
    public void test1() {
        // long start = System.currentTimeMillis();
        Instant start = Instant.now();

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinCalculate(0L, 10000000000L);

        long sum = pool.invoke(task);
        System.out.println(sum);

        // long end = System.currentTimeMillis();
        // System.out.println("耗费的时间为: " + (end - start)); //112-1953-1988-2654-2647-20663-113808

        Instant end = Instant.now();
        System.out.println("耗费时间为:" + Duration.between(start, end).toMillis());
    }

    @Test
    public void test2() {
        long start = System.currentTimeMillis();

        long sum = 0L;

        for (long i = 0L; i <= 10000000000L; i++) {
            sum += i;
        }

        System.out.println(sum);

        long end = System.currentTimeMillis();

        System.out.println("耗费的时间为: " + (end - start)); //34-3174-3132-4227-4223-31583
    }

    /**
     * java 8 并行流
     */
    @Test
    public void test3() {
        long start = System.currentTimeMillis();

        Long sum = LongStream.rangeClosed(0L, 10000000000L)
            .parallel()
            .sum();

        System.out.println(sum);

        long end = System.currentTimeMillis();

        System.out.println("耗费的时间为: " + (end - start)); //2061-2053-2086-18926
    }

}
