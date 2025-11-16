import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasscoderTest {

    @Test
    void testPasscode(){
        System.out.println(new BCryptPasswordEncoder().encode("123"));
//        "$2a$10$aEXnwKPgl77z0oHF5QvkSunUVyCVbDnJl4k1dCK.xdZbsEMIx1vsi"
//        $2a$10$n.H7KycDmApVzDtSY82ZCeiZWxaFBVm/NImxmvPDlDqWou2CtnxQm
    }
}
