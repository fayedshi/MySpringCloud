import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        store.stream().flatMap(list->list.stream()).toList().forEach(x->System.out.println(x));
    }

    @Test
    void testThen() {
        Mono.just("hello").then(Mono.empty())
                .flatMap(x -> Mono.just("test"))
                .subscribe(System.out::println);
    }

}
