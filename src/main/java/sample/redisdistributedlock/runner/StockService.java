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
    private final RedisTemplate txRedisTemplate;

    //@Transactional
    public void supplyStock() {
        String key = "stock";
        String value = (String) txRedisTemplate.opsForValue()
                .get(key);

        if(value != null) {
            txRedisTemplate.opsForValue().set("stock", String.valueOf(Integer.parseInt(value) + 1));
            log.info("[SS::supplyStock] Current stock {}, After Stock {} !!", value, txRedisTemplate.opsForValue().get("stock"));
        }
    }
}
