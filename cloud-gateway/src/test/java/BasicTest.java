import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.LongAdder;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class BasicTest {

    @Test
    void testPasscode() {
        System.out.println(new BCryptPasswordEncoder().encode("123"));
//        "$2a$10$aEXnwKPgl77z0oHF5QvkSunUVyCVbDnJl4k1dCK.xdZbsEMIx1vsi"
//        $2a$10$n.H7KycDmApVzDtSY82ZCeiZWxaFBVm/NImxmvPDlDqWou2CtnxQm
    }

    @Test
    void testGrantAuthority() {
        Map<String, Collection<? extends GrantedAuthority>> claims = new HashMap<>();
        claims.put("roles", List.of(() -> "USER", () -> "MEMBER"));

        Assertions.assertEquals(((List<GrantedAuthority>) claims.get("roles")).get(0).getAuthority(), "USER");
    }

    @Test
    public void testNoData() {
        Mono.just("test1")
                .flatMap(val -> Mono.empty())
                .switchIfEmpty(getData())
                .subscribe(System.out::println);
    }

    @Test
    public void testHasData() {
        Mono.just("test1")
                .flatMap(val -> Mono.just("step 1"))
                .switchIfEmpty(Mono.empty())
                .subscribe(System.out::println);
    }

    Mono<String> getData() {
        return Mono.just("from getData");
    }

    @Test
    void testFlatMap() {
        List<List<String>> store = List.of(List.of("account1", "12RMB"), List.of("account", "any"));
        store.stream().flatMap(list -> list.stream()).toList().forEach(x -> System.out.println(x));
    }

    @Test
    void testThen() {
        Mono.just("hello").then(Mono.empty())
                .flatMap(x -> Mono.just("test"))
                .subscribe(System.out::println);
    }

    @Test
    void testFlux() {
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    if (i > 4) {
                        throw new RuntimeException(" it's 4");
                    }
                },
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"));

        Flux<Integer> intsFlux = Flux.range(1, 4);
        intsFlux.doOnNext(x -> System.out.println("inline" + x)).subscribe();
    }

    @Test
    void testGenerate() {
        Flux.generate(
                () -> 0,
                (state, sink) -> {
//                    sink.next("3 x " + state + " = " + 3 * state);
                    sink.next(state);
                    if (state == 10) sink.complete();
                    return state + 1;
                }).doOnComplete(() -> System.out.println("completed")).doOnNext(System.out::println).subscribe();
//                .subscribe(state-> System.out.printf("%s",state));
//                .log().subscribe().;;
    }

    @Test
    void testCreate() throws InterruptedException {
        Flux.create(sink -> {
                    for (int i = 0; i < 100; i++) {
                        int finalI = i;
                        new Thread(() -> {
                            try {
                                Thread.sleep(100 * new Random().nextInt(10));
                                sink.next("int " + finalI);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }).start();
//                        task.run();
//                        sink.next(i + "");
//                        System.out.println(Thread.currentThread().getName());
                    }
                }
        ).doOnNext(System.out::println).subscribe();
        Thread.sleep(4000);


        interface MyEventListener<T> {
            void onDataChunk(List<T> chunk);

            void processComplete();
        }
        interface EventProcessor {
            void register(MyEventListener myEventListener);
        }
        EventProcessor processor = (eventListner) -> {
        };
//        EventProcessor myEventProcessor = new EventProcessor();
        Flux<String> bridge = Flux.create(sink -> processor.register(
                new MyEventListener<String>() {
                    public void onDataChunk(List<String> chunk) {
                        for (String s : chunk) {
                            sink.next(s);
                        }
                    }

                    public void processComplete() {
                        sink.complete();
                    }
                }));
        bridge.subscribe();
    }

    @Test
    void testHook() {
        SampleSubscriber<Integer> ss = new SampleSubscriber<Integer>();
        Flux<Integer> intsFlux = Flux.range(1, 4);
        intsFlux.subscribe(ss);
    }

    class SampleSubscriber<T> extends BaseSubscriber<T> {
        @Override
        public void hookOnSubscribe(Subscription subscription) {
            System.out.println("Subscribed");
            request(3);
        }

        @Override
        public void hookOnComplete() {
            System.out.println("completed");
        }


        @Override
        public void hookOnNext(T value) {
            request(2);
            System.out.println(value);
//            cancel();
        }
    }

    @Test
    void testPublishOn() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);
        Flux.range(1, 3)
                .map(i -> 10 + i)
                .log()
                .publishOn(s) // the subsequent operators run on s
                .map(i -> "value " + i).log().subscribe();

//        new Thread(() -> flux.subscribe(System.out::println)).start();
//        Thread.sleep(2000);
//        flux.log().subscribe();
    }

    @Test
    void testSubscribOn() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);
        final Flux<String> flux = Flux
                .range(1, 3)
//                .log("range log")
                .map(i -> 10 + i)
                .log("map log")
                .subscribeOn(s) // the subsequent operators run from start of sequence
                .map(i -> "value " + i);

        new Thread(() -> flux.subscribe()).start();
        Thread.sleep(2000);
//        flux.log().subscribe();
    }

    @Test
    void testErrorHandling() {
        Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (9 / i)) //this triggers an error with 0
                .onErrorReturn("Divided by zero :(").log().subscribe();
    }

    @Test
    void testCompletableFuture() {
        CompletableFuture
                .runAsync(() -> System.out.println("t1"))
                .thenRun(() -> System.out.println("t2"))
                .thenRun(() -> System.out.println("t3"));
    }

    @Test
    void testDoFinally() {
//        Stats stats = new Stats();
        LongAdder statsCancel = new LongAdder();

//        Flux<String> flux =
        Flux.just("foo", "bar")
                .doOnSubscribe(s -> System.out.println(s))
                .doFinally(type -> {
//                            stats.stopTimerAndRecordTiming();
                    if (type == SignalType.CANCEL)
                        statsCancel.increment();
                })
                .take(1).log().subscribe();
    }

    @Test
    void testInterval() throws InterruptedException {
        Flux.interval(Duration.ofMillis(200))
                .map(input -> {
                            if (input > 5) {
                                throw new RuntimeException();
                            }
                            return input + " element";
                        }
                ).onErrorReturn("Oh NO")
                .log().subscribe();
        Thread.sleep(2000);
    }

    @Test
    void testRetry() throws InterruptedException {
        Flux.interval(Duration.ofMillis(200))
                .map(input -> {
                            if (input > 2) {
                                throw new RuntimeException();
                            }
                            return input + " element";
                        }
                ).retry(1)
                .elapsed()
                .log().subscribe();
        Thread.sleep(2000);
    }

    @Test
    void testRetryWhen(){
        Flux<String> flux = Flux
                .<String>error(new IllegalArgumentException())
                .doOnError(System.out::println).onErrorMap(a->new RuntimeException("error happened"))
                .retryWhen(Retry.from(companion ->
                        companion.take(3)));
        flux.subscribe(System.out::println);
    }

}

