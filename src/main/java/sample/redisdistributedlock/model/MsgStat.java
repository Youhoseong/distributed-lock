package sample.redisdistributedlock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "msg-stat")
public class MsgStat {
    @Id
    private String msgStatId;
    private boolean available;
    private long timestamp;
}
