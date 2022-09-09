package sample.redisdistributedlock.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MultiTransactionRunner implements CommandLineRunner {
    @Qualifier("txRedisTemplate")
    private final RedisTemplate redisTemplate;


    @Override
    public void run(String... args) throws Exception {
        log.info("hi");
    }
}
