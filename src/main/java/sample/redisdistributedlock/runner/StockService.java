package sample.redisdistributedlock.runner;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    @Qualifier("txRedisTemplate")
    private final RedisTemplate redisTemplate;

    public void supplyStock() {
        String value = (String) redisTemplate.opsForValue()
                .get("tx-stock");

        if(value != null) {
            redisTemplate.opsForValue().set("tx-stock", String.valueOf(Integer.parseInt(value) + 1));
            log.info("[SS::supplyStock] Current stock {}, After Stock {} !!", value, Integer.valueOf(value)+1);
        }
    }
}
