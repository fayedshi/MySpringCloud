package com.glide.springcloud;

import java.util.ArrayList;
import java.util.function.*;

public class TestFunctionInterface {


    public static void main(String[] args) {
        System.out.println("bipred "+testBiPredicate("abc", "123", (p, q) -> !p.isEmpty() && !q.isEmpty()));
        System.out.println(validate("abc", s -> !s.isEmpty(), s -> s.length() > 2));
        getString(() -> "hey");
        String[] strArray = {"张三,30", "李四,21", "王五,18"};
        print(strArray,
                s -> System.out.print("姓名：" + s.split(",")[0]),
                s -> System.out.println(" 年龄：" + s.split(",")[1]));

        convert("test", input -> input.length() + "idea".length());
        convert("hello", input -> input + " idea", newStr -> newStr + " appended new String");
        toUpper("hello", input -> input + " idea", newStr -> newStr);
    }


    // supplier interface
    private static boolean testBiPredicate(String s2, String s1, BiPredicate<String, String> predicate) {
        return predicate.test(s1, s2);
    }

    private static boolean validate(String s2, Predicate<String> predicate, Predicate<String> pre) {
        return predicate.and(pre).test(s2);
    }

    // supplier interface
    private static void getString(Supplier<String> con1) {
        con1.get();
    }

    private static void print(String[] strArray, Consumer<String> con1, Consumer<String> con2) {
        for (String str : strArray) {
            con1.andThen(con2).accept(str);
        }
    }

    private static void convert(String s, Function<String, Integer> fun1) {
        int n1 = fun1.apply(s);
        System.out.println(n1);
    }

    private static void convert(String s, Function<String, String> fun1, Function<String, String> fun2) {
        String s1 = fun1.andThen(fun2).apply(s);
        System.out.println(s1);
    }

    private static void toUpper(String s, Function<String, String> fun1, Function<String, String> fun2) {
        String s1 = fun2.apply(fun1.apply(s).toUpperCase());
        System.out.println(s1);
    }

}
