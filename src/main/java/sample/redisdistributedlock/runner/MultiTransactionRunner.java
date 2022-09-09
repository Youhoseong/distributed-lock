package sample.redisdistributedlock.runner;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class MultiTransactionRunner implements CommandLineRunner {
    @Qualifier("txRedisTemplate")
    private final RedisTemplate redisTemplate;
    private final StockService stockService;

    @Override
    public void run(String... args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        redisTemplate.opsForValue().set("tx-stock", String.valueOf(1000));
        IntStream.rangeClosed(1, 30)
                .mapToObj(it -> new Thread(doTx(), "Tx-Stock-".concat(String.valueOf(it))))
                .map(tr -> {
                    tr.start();
                    return tr;
                })
                .forEach(this::join);

        stopWatch.stop();

    }

    private Runnable doTx() {
        return () -> {
            Stream.iterate(0, n -> n + 1)
                    .limit(100)
                    .forEach((it) -> stockService.supplyStock());
        };
    }

    @SneakyThrows(InterruptedException.class)
    private void join(Thread thread) {
        thread.join();
    }

}
