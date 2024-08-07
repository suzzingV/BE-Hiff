package hiff.hiff.behiff.global.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Transactional
public class RedisService {

    private final RedisTemplate<String, String> strRedisTemplate;
    private final RedisTemplate<String, Integer> integerRedisTemplate;
    public static final String EVALUATION_PREFIX = "eval_";
    public static final String NOT_EXIST = "false";
    private static final Duration EVALUATION_DURATION = Duration.ofDays(1);

    public void setStrValue(String key, String data, Duration duration) {
        ValueOperations<String, String> values = strRedisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public void updateIntValue(String key) {
        ValueOperations<String, Integer> values = integerRedisTemplate.opsForValue();
        int count = getIntValue(key);
        if (count == 0) {
            values.set(key, 1);
        } else {
            if(count == 4) {
                values.set(key, 5, EVALUATION_DURATION);
            } else {
                values.set(key, count + 1);
            }
        }
    }

    @Transactional(readOnly = true)
    public String getStrValue(String key) {
        ValueOperations<String, String> values = strRedisTemplate.opsForValue();
        if (values.get(key) == null) {
            return NOT_EXIST;
        }
        return values.get(key);
    }

    @Transactional(readOnly = true)
    public int getIntValue(String key) {
        ValueOperations<String, Integer> values = integerRedisTemplate.opsForValue();
        if (values.get(key) == null) {
            return 0;
        }
        return values.get(key);
    }

    public void delete(String key) {
        strRedisTemplate.delete(key);
    }
}
