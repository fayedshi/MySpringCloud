import com.glide.springcloud.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JwtTest {


    @Test
    void testJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin");
        claims.put("department", "IT");
        String token = JwtUtil.generateToken(claims, "allen");

        Claims claim = JwtUtil.validateToken(token);
        assertEquals(claim.getSubject(), "allen");
        assertEquals(claim.get("role"), "admin");

    }

    @Test
    void testString()
    {
        System.out.printf("[dl.queue] %s Received message: , deliveryTag:", LocalDateTime.now());
    }
}
