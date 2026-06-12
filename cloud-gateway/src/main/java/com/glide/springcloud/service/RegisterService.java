package com.glide.springcloud.service;

import com.glide.springcloud.config.RabbitConfig;
import com.glide.springcloud.mapper.CloudUserMapper;
import com.glide.springcloud.mapper.UserRolesMapper;
import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.model.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class RegisterService {
    @Autowired
    CloudUserMapper userMapper;

    @Autowired
    UserRolesMapper userRolesMapper;
    @Autowired
    BCryptPasswordEncoder bcEncoder;

    @Autowired
    RabbitProducer rabbitProducer;

    @Transactional
    public Map<String, Object> register(CloudUser cloudUser) {
        try {
            CloudUser user = userMapper.selectUserWithAuth(cloudUser.getUsername());
            if (user != null) {
                return Map.of("Code", 400, "Message", "Registration failed, Username exists.");
            }
            cloudUser.setPassword(bcEncoder.encode((cloudUser.getPassword())));
            cloudUser.setAccountNonExpired(true);
            cloudUser.setAccountNonExpired(true);
            cloudUser.setCredentialsNonExpired(true);
            cloudUser.setAccountNonLocked(true);
            cloudUser.setEnabled(true);
            userMapper.insert(cloudUser);
            userRolesMapper.insert(new UserRoles(cloudUser.getId(), 3));
            sendMessage(cloudUser.getUsername());
            return Map.of("Code", 200, "Message", "Success");
        } catch (Exception e) {
            return Map.of("Code", -1, "Message", e.getMessage());
        }
    }

    // send registration message to normal que with ttl, after expired, the message will be transferred to  DLQ
    private void sendMessage(String userName) {
        CompletableFuture.runAsync(() -> rabbitProducer
                        .sendMessage(String.format("User %s is registered.", userName), RabbitConfig.ROUTING_KEY_NORMAL, 8000))
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        System.out.println("failed: " + exception);
                    } else {
                        System.out.println("message sent.");
                    }
                });
    }
}
