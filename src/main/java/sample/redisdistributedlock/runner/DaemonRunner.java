package sample.redisdistributedlock.runner;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class DaemonRunner implements CommandLineRunner {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;

    @Override
    public void run(String... args) {
        redisTemplate.opsForValue().set("stock", String.valueOf(1000));

        IntStream.rangeClosed(1, 5)
                .mapToObj(it -> new Thread(doCompetition(String.valueOf(it)), "Stock-".concat(String.valueOf(it))))
                .forEach(Thread::start);

    }


    private Runnable doCompetition(String key) {
        return () -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            RLock lock = redissonClient.getLock("lock1");

            for(int i=0; i<10; i++) {
                try {
                    if(lock.tryLock(1, 3, TimeUnit.SECONDS)) {
                        String stock = redisTemplate.opsForValue().get("stock");
                        redisTemplate.opsForValue()
                                .set("stock", String.valueOf(Integer.valueOf(stock) - 1));

                        log.info("[DR::doCompetition] Current stock {}, After Stock {} !! {}", stock, Integer.valueOf(stock)-1);
                        lock.unlock();
                    }
                } catch (Throwable t) {
                    log.error("why not?");
                }
            }

            stopWatch.stop();
            log.info("[DR::doCompetition] Threadname {}, Task time={}ms", "Stock-".concat(key), stopWatch.getTotalTimeMillis());

        };
    }

    @SneakyThrows
    private void sleep(Long millis) {
        Thread.sleep(millis);
    }
}
