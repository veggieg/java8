package com.veggieg.tutorials.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 一、Stream API 的操作步骤：
 * <p>
 * 1. 创建 Stream
 * <p>
 * 2. 中间操作
 * <p>
 * 3. 终止操作(终端操作)
 *
 * @author veggieg
 * @since 2018-07-25 0:34
 */
public class TestStreamAPI {
    //----------------------------------------------------- 1. 创建 Stream
    @Test
    public void test1() {
        //1. Collection 提供了两个方法  stream() 与 parallelStream()
        List<String> list = new ArrayList<>();
        Stream<String> stream = list.stream(); //获取一个顺序流
        Stream<String> parallelStream = list.parallelStream(); //获取一个并行流

        //2. 通过 Arrays 中的 stream() 获取一个数组流
        Integer[] nums = new Integer[10];
        Stream<Integer> stream1 = Arrays.stream(nums);

        //3. 通过 Stream 类中静态方法 of()
        Stream<Integer> stream2 = Stream.of(1, 2, 3, 4, 5, 6);

        //4. 创建无限流
        //迭代
        Stream<Integer> stream3 = Stream.iterate(0, (x) -> x + 2).limit(10);
        stream3.forEach(System.out::println);

        //生成
        Stream<Double> stream4 = Stream.generate(Math::random).limit(2);
        stream4.forEach(System.out::println);

    }

    //----------------------------------------------------- 2. 中间操作
    List<Employee> emps = Arrays.asList(
        new Employee(102, "李四", 59, 6666.66),
        new Employee(101, "张三", 18, 9999.99),
        new Employee(104, "赵六", 8, 7777.77),
        new Employee(104, "赵六", 8, 7777.77),
        new Employee(103, "王五", 28, 3333.33),
        new Employee(105, "田七", 38, 8888.88),
        new Employee(105, "田七", 38, 8888.88)
    );

    // 内部迭代：迭代操作 Stream API 内部完成

    /**
     * 筛选与切片
     * filter——接收 Lambda ， 从流中排除某些元素。
     * limit——截断流，使其元素不超过给定数量。
     * skip(n) —— 跳过元素，返回一个扔掉了前 n 个元素的流。若流中元素不足 n 个，则返回一个空流。与 limit(n) 互补
     * distinct——筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素
     */

    @Test
    public void test2() {
        // 所有的中间操作不会做任何的处理
        Stream<Employee> stream = emps.stream()
            .filter((e) -> {
                System.out.println("测试中间操作");
                return e.getAge() <= 35;
            });

        // 只有当做终止操作时，所有的中间操作会一次性的全部执行，称为“惰性求值”
        stream.forEach(System.out::println);
    }

    //外部迭代
    @Test
    public void test3() {
        Iterator<Employee> it = emps.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    @Test
    public void test4() {
        emps.stream()
            .filter((e) -> {
                System.out.println("短路！"); // 类似 &&  ||
                return e.getSalary() >= 5000;
            })
            .limit(3)
            .forEach(System.out::println);
    }

    @Test
    public void test5() {
        emps.parallelStream()
            .filter((e) -> e.getSalary() >= 5000)
            .skip(3)
            .forEach(System.out::println);

        emps.parallelStream()
            .skip(3)
            .filter((e) -> e.getSalary() >= 5000)
            .forEach(System.out::println);
    }

    @Test
    public void test6() {
        emps.stream()
            .distinct()
            .forEach(System.out::println);
    }

    /**
     * 映射
     * map——接收 Lambda ， 将元素转换成其他形式或提取信息。接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
     * flatMap——接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
     */
    @Test
    public void test7() {
        emps.stream()
            .map((e) -> e.getName())
            .forEach(System.out::println);

        System.out.println("-------------------------------------------");

        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");

        Stream<String> stream = list.stream()
            .map(String::toUpperCase);
        stream.forEach(System.out::println);

        System.out.println("---------------------------------------------");

        Stream<Stream<Character>> stream2 = list.stream()
            .map(TestStreamAPI::filterCharacter); // {{a, a, a}, {b, b, b}, ...} 类似 add

        stream2.forEach((sm) -> {
            sm.forEach(System.out::println);
        });

        System.out.println("---------------------------------------------");

        Stream<Character> stream3 = list.stream()
            .flatMap(TestStreamAPI::filterCharacter);// {a, a, a, b, b, b, ...} 类似 addAll

        stream3.forEach(System.out::println);
    }

    @Test
    public void test8() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd");

        List<Object> list2 = new ArrayList<>();
        list2.add(11);
        list2.add(22);

        list2.addAll(list);
        list2.add(list);

        System.out.println(list2);
    }

    public static Stream<Character> filterCharacter(String str) {/// add(Object obj) addAll(Collection coll)
        List<Character> list = new ArrayList<>();

        for (Character ch : str.toCharArray()) {
            list.add(ch);
        }

        return list.stream();
    }

    /**
     * sorted()——自然排序(Comparable)
     * sorted(Comparator com)——定制排序(Comparator)
     */
    @Test
    public void test9() {
        emps.stream()
            .map(Employee::getName)
            .sorted()
            .forEach(System.out::println);

        System.out.println("------------------------------------");

        emps.stream()
            .sorted((x, y) -> {
                if (x.getAge() == y.getAge()) {
                    return x.getName().compareTo(y.getName());
                } else {
                    return Integer.compare(x.getAge(), y.getAge());
                }
            }).forEach(System.out::println);
    }

    //----------------------------------------------------- 3. 终止操作

    List<Employee> employeeList = Arrays.asList(
        new Employee(101, "张三", 18, 9999.99, Employee.Status.FREE),
        new Employee(102, "李四", 59, 6666.66, Employee.Status.BUSY),
        new Employee(103, "王五", 28, 3333.33, Employee.Status.VOCATION),
        new Employee(104, "赵六", 8, 7777.77, Employee.Status.FREE),
        new Employee(105, "田七", 38, 8888.88, Employee.Status.BUSY)
    );

    /**
     * allMatch——检查是否匹配所有元素
     * anyMatch——检查是否至少匹配一个元素
     * noneMatch——检查是否没有匹配的元素
     * findFirst——返回第一个元素
     * findAny——返回当前流中的任意元素
     * count——返回流中元素的总个数
     * max——返回流中最大值
     * min——返回流中最小值
     */
    @Test
    public void test10() {
        boolean bool = employeeList.stream().allMatch(e -> e.getStatus().equals(Employee.Status.BUSY));
        System.out.println(bool);

        boolean bool2 = employeeList.stream().anyMatch(e -> e.getStatus().equals(Employee.Status.BUSY));
        System.out.println(bool2);

        boolean bool3 = employeeList.stream().noneMatch(e -> e.getStatus().equals(Employee.Status.BUSY));
        System.out.println(bool3);

        Optional<Employee> optional = employeeList.stream()
            .sorted((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()))
            .findFirst();
        System.out.println(optional.get());

        // Optional<Employee> op2 = employeeList.stream()
        Optional<Employee> op2 = employeeList.parallelStream()
            .filter(e -> e.getStatus().equals(Employee.Status.FREE))
            .findAny();
        System.out.println(op2.get());
    }

    @Test
    public void test11() {
        final long count = employeeList.stream().count();
        System.out.println(count);

        Optional<Employee> op1 = employeeList.stream().max((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
        System.out.println(op1.get());

        Optional<Double> op2 = employeeList.stream().map(Employee::getSalary).min(Double::compare);
        System.out.println(op2.get());
    }

    /**
     * 注意：流进行了终止操作后，不能再次使用
     */
    @Test
    public void test12(){
        Stream<Employee> stream = emps.stream()
            .filter((e) -> e.getStatus().equals(Employee.Status.FREE));

        long count = stream.count();

        stream.map(Employee::getSalary)
            .max(Double::compare);
    }

    /**
     * 归约 reduce(T identity, BinaryOperator) / reduce(BinaryOperator) ——可以将流中元素反复结合起来，得到一个值。
     */
    @Test
    public void test13() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Integer sum = list.stream().reduce(0, (x, y) -> x + y);
        System.out.println(sum);

        Optional<Double> op = employeeList.stream().map(Employee::getSalary).reduce(Double::sum);
        System.out.println(op.get());
    }


    //需求：搜索名字中 “六” 出现的次数
    @Test
    public void test14(){
        Optional<Integer> sum = employeeList.stream()
            .map(Employee::getName)
            .flatMap(TestStreamAPI::filterCharacter)
            .map((ch) -> {
                if (ch.equals('六')) {
                    return 1;
                } else {
                    return 0;
                }
            }).reduce(Integer::sum);

        System.out.println(sum.get());
    }

    /**
     * 	//collect——将流转换为其他形式。接收一个 Collector接口的实现，用于给Stream中元素做汇总的方法
     */
    @Test
    public void test15() {
        List<String> list = employeeList.stream().map(Employee::getName).collect(Collectors.toList());
        list.forEach(System.out::println);

        Set<String> set = employeeList.stream().map(Employee::getName).collect(Collectors.toSet());
        set.forEach(System.out::println);

        HashSet<String> hashSet = employeeList.stream().map(Employee::getName).collect(Collectors.toCollection(HashSet::new));
        hashSet.forEach(System.out::println);
    }

    @Test
    public void test16() {
        /// 总数
        Long count = employeeList.stream().collect(Collectors.counting());
        System.out.println(count);

        /// 平均值
        Double avg = employeeList.stream()
            // .map(Employee::getSalary)
            .collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println(avg);

        /// 总和
        Double sum = employeeList.stream().collect(Collectors.summingDouble(Employee::getSalary));
        System.out.println(sum);

        /// 最大值
        Optional<Employee> employee = employeeList.stream().collect(Collectors.maxBy((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())));
        System.out.println(employee.get());

        /// 最小值
        final Optional<Double> min = employeeList.stream().map(Employee::getSalary).collect(Collectors.minBy(Double::compare));
        System.out.println(min.get());
    }

    /// 分组
    @Test
    public void test17() {
        final Map<Employee.Status, List<Employee>> map = employeeList.stream().collect(Collectors.groupingBy(Employee::getStatus));
        System.out.println(map);
    }

    /// 多级分组
    @Test
    public void test18() {
        final Map<Employee.Status, Map<String, List<Employee>>> map = employeeList.stream()
            .collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
                if (e.getAge() <= 35) {
                    return "青年";
                } else if (e.getAge() <= 50) {
                    return "中年";
                } else {
                    return "老年";
                }
            })));
        System.out.println(map);
    }

    /// 分区
    @Test
    public void test19() {
        final Map<Boolean, List<Employee>> map = employeeList.stream().collect(Collectors.partitioningBy((e) -> e.getSalary() > 8000));
        System.out.println(map);
    }

    //
    @Test
    public void test20(){
        String str = employeeList.stream()
            .map(Employee::getName)
            .collect(Collectors.joining("," , "[", "]"));

        System.out.println(str);
    }

    @Test
    public void test21(){
        Optional<Double> sum = employeeList.stream()
            .map(Employee::getSalary)
            .collect(Collectors.reducing(Double::sum));

        System.out.println(sum.get());
    }

    public void test22() {
        DoubleSummaryStatistics summaryStatistics = employeeList.stream()
            .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println(summaryStatistics.getAverage());
        System.out.println(summaryStatistics.getCount());
        System.out.println(summaryStatistics.getMax());

    }
}
