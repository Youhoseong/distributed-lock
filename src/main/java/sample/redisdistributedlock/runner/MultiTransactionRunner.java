package sample.redisdistributedlock.runner;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class MultiTransactionRunner implements CommandLineRunner {
    @Qualifier("txRedisTemplate")
    private final RedisTemplate txRedisTemplate;
    private final StockService stockService;

    @Override
    public void run(String... args) {
        log.info("[MTR::run] Check stockService proxy class {}", stockService.getClass());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String key = "stock";
        txRedisTemplate.opsForValue().set(key, String.valueOf(1001));

        List<Thread> threads = IntStream.rangeClosed(1, 3)
                .mapToObj(it -> new Thread(doTx(), "Tx-Stock-".concat(String.valueOf(it))))
                .map(tr -> {
                    tr.start();
                    return tr;
                })
                .collect(Collectors.toList());

        threads.forEach(this::join);
        stopWatch.stop();
        log.info("[MTR::run] Threadname {} All task done, runtime={}ms", Thread.currentThread().getName(), stopWatch.getTotalTimeMillis());
    }

    private Runnable doTx() {
        return () -> Stream.iterate(0, n -> n+1)
                        .limit(2)
                        .forEach(it -> stockService.supplyStock());
    }

    @SneakyThrows(InterruptedException.class)
    private void join(Thread thread) {
        thread.join();
    }

}
