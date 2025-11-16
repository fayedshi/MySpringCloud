//import io.jsonwebtoken.impl.TextCodec;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.util.function.*;
//import io.jsonwebtoken.J


public class TestFunctionInterface {


    public static void main(String[] args) {
        System.out.println("bipred " + testBiPredicate("abc", "123", (p, q) -> !p.isEmpty() && !q.isEmpty()));
        System.out.println(validate("abc", s -> !s.isEmpty(), s -> s.length() > 2));
        getString(() -> "hey");
        String[] strArray = {"张三,30", "李四,21", "王五,18"};
        print(strArray,
                s -> System.out.print("姓名：" + s.split(",")[0]),
                s -> System.out.println(" 年龄：" + s.split(",")[1]));

        convert("test", input -> input.length() + "idea".length());
        convert("hello", input -> input + " idea", newStr -> newStr + " appended new String");
        toUpper("hello", input -> input + " idea", newStr -> newStr);

        System.out.println(convert("home", (s) -> s.indexOf("o")));

        new SecretKeySpec("hello".getBytes(), SignatureAlgorithm.HS256.getJcaName());
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        System.out.println(antPathMatcher.match("/home/user/**", "/home/user/1/6/6"));



        Mono.just("hello")
                .doOnNext(str -> {
                    System.out.println("转换前: " + str);
                })
                .map(String::toUpperCase)
                .subscribe(System.out::println);

        Mono<String> obj = Mono.just("test");
        obj.subscribe(x -> System.out.println(x));


//
//        String pass=TextCodec.BASE64.encode("hello");
//        byte[] bytes = TextCodec.BASE64.decode(pass);
//        String decPass=new String(bytes);

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

//    print()

    // convert("home", (s)->s.indexOf("o"))
    private static int convert(String s, Function<String, Integer> fun1) {
        int n1 = fun1.apply(s);
        return n1;
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
