package hiff.hiff.behiff.global.common.redis;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Integer> integerRedisTemplate;
    public static final String EVALUATION_PREFIX = "eval_";
    public static final String NOT_EXIST = "false";
    public static final String HOBBY_PREFIX = "hobby_";
    public static final String MBTI_PREFIX = "mbti_";
    public static final String LIFESTYLE_PREFIX = "lifestyle_";
    public static final String INCOME_PREFIX = "income_";
    public static final String DAILY_MATCHING_PREFIX = "daily_";
    public static final String PAID_DAILY_MATCHING_PREFIX = "paidDaily_";
    public static final String HIFF_MATCHING_PREFIX = "hiff_";
    private static final Duration EVALUATION_DURATION = Duration.ofDays(1);
    public static final Duration MATCHING_DURATION = Duration.ofDays(1);

    public static final Duration HIFF_MATCHING_DURATION = Duration.ofDays(2);

    public void setValue(String key, Object data, Duration duration) {
        String value = String.valueOf(data);
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }

    public void updateIntValue(String key) {
        ValueOperations<String, Integer> values = integerRedisTemplate.opsForValue();
        int count = getIntValue(key);
        if (count == 0) {
            values.set(key, 1);
        } else {
            if (count == 4) {
                values.set(key, 5, EVALUATION_DURATION);
            } else {
                values.set(key, count + 1);
            }
        }
    }

    @Transactional(readOnly = true)
    public String getStrValue(Object key) {
        String realKey = String.valueOf(key);
        ValueOperations<String, String> values = redisTemplate.opsForValue();
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

    @Transactional(readOnly = true)
    public Long getLongValue(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return 0L;
        }
        return Long.parseLong(values.get(key));
    }

    @Transactional(readOnly = true)
    public List<String> scanKeysWithPrefix(String prefix) {
        List<String> keys = new ArrayList<>();

        Cursor<byte[]> cursor = Objects.requireNonNull(redisTemplate.getConnectionFactory())
            .getConnection()
            .keyCommands().scan(
                ScanOptions.scanOptions().match(prefix + "*").count(1000).build()
            );

        while (cursor.hasNext()) {
            String key = new String(cursor.next(), StandardCharsets.UTF_8);

            keys.add(key);
        }

        return keys;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
