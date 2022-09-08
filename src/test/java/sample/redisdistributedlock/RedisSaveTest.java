package sample.redisdistributedlock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class RedisSaveTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void test() {
        redisTemplate.opsForValue().set("key1", "value1");
    }
}
