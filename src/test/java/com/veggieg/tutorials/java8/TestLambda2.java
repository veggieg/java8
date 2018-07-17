package com.veggieg.tutorials.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 一、Lambda 表达式的基础语法：Java8中引入了一个新的操作符 "->" 该操作符称为箭头操作符或 Lambda 操作符
 * 箭头操作符将 Lambda 表达式拆分成两部分：
 * <p>
 * 左侧：Lambda 表达式的参数列表
 * 右侧：Lambda 表达式中所需执行的功能， 即 Lambda 主体
 * <p>
 * 语法格式一：无参数，无返回值 @see test1
 * () -> System.out.println("Hello Lambda!");
 * <p>
 * 语法格式二：有一个参数，并且无返回值 @see test2
 * (x) -> System.out.println(x)
 * <p>
 * 语法格式三：若只有一个参数，小括号可以省略不写 @see test2
 * x -> System.out.println(x)
 * <p>
 * 语法格式四：有两个以上的参数，有返回值，并且 Lambda 体中有多条语句 @see test3
 * Comparator<Integer> com = (x, y) -> {
 * System.out.println("函数式接口");
 * return Integer.compare(x, y);
 * };
 * <p>
 * 语法格式五：若 Lambda 体中只有一条语句， return 和 大括号都可以省略不写 @see test4
 * Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
 * <p>
 * 语法格式六：Lambda 表达式的参数列表的数据类型可以省略不写，因为JVM编译器通过上下文推断出，数据类型，即“类型推断” @see test5
 * (Integer x, Integer y) -> Integer.compare(x, y);
 * <p>
 * 上联：左右遇一括号省 (左遇一个参数省括号, 右遇一条语句大括号省)
 * 下联：左侧推断类型省 (左侧参数列, 参数列表的类型可以省略)
 * 横批：能省则省
 * <p>
 * Lambda语法
 * (1) () -> {}
 * (2) () -> "Raoul"
 * (3) () -> {return"Mario";}
 * (4) (Integer i) -> return "Alan" + i;
 * (5) (Strings) -> {"IronMan";}
 * 答案：只有4和5是无效的Lambda。
 * (1) 这个Lambda没有参数，并返回 void 。它类似于主体为空的方法： public void run() {} 。
 * (2) 这个Lambda没有参数，并返回 String 作为表达式。
 * (3) 这个Lambda没有参数，并返回 String （利用显示返回语句）。
 * (4) return 是一个控制流语句。要使此Lambda有效，需要使花括号，如下所示：
 * (Integeri) -> {return "Alan" + i;} 。
 * (5)“Iron Man”是一个表达式，不是一个语句。要使此Lambda有效，你可以去除花括号
 * 和分号，如下所示： (String s) -> "Iron Man" 。或者如果你喜欢，可以使用显式返回语
 * 句，如下所示： (Strings)->{return "IronMan";} 。
 * <p>
 * 二、Lambda 表达式需要“函数式接口”的支持 @see test6
 * 函数式接口：只定义一个抽象方法的接口。 可以使用注解 @FunctionalInterface 修饰, 可以检查是否是函数式接口
 * 函数式接口的抽象方法的签名基本上就是Lambda表达式的签名。我们将这种抽象方法叫作函数描述符
 *
 * @author veggieg
 * @since 2018-07-14 2:14
 */
public class TestLambda2 {
    @Test
    public void test1() {
        int num = 0;//jdk 1.7 前，必须是 final

        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World!" + num);
            }
        };

        r.run();

        System.out.println("-------------------------------");

        // Runnable r1 = () -> System.out.println("Hello Lambda!" + num++);
        Runnable r1 = () -> System.out.println("Hello Lambda!" + num);
        r1.run();
    }

    @Test
    public void test2() {
        Consumer<String> con = x -> System.out.println(x);
        con.accept("PUBG");
    }

    @Test
    public void test3() {
        Comparator<Integer> com = (x, y) -> {
            System.out.println("函数式接口");
            return Integer.compare(x, y);
        };
    }

    @Test
    public void test4() {
        Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
        // Comparator<Integer> com = (Integer x, Integer y) -> Integer.compare(x, y);
        // Comparator<Integer> com = Comparator.comparingInt(Integer::intValue);
        // Comparator<Integer> com = Comparator.comparingInt(x -> x);
    }

    @Test
    public void test5() {
        /// 正确
        String[] strs = {"aaa", "bbb", "ccc"};

        /// 错误, 推断不出
        // String[] strs;
        // strs = {"aaa", "bbb", "ccc"};


        List<String> list = new ArrayList<>();

        show(new HashMap<>());
    }

    public void show(Map<String, Integer> map) {

    }

    /// 对一个数进行运算
    @Test
    public void test6(){
        Integer num = operation(100, x -> x * x);
        System.out.println(num);

        System.out.println(operation(200, (y) -> y + 200));
    }

    public Integer operation(Integer num, MyFunction mf){
        return mf.op(num);
    }
}
