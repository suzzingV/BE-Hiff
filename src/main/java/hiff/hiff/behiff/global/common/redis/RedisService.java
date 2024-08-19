package hiff.hiff.behiff.global.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Transactional
public class RedisService {

    private final RedisTemplate<String, String> strRedisTemplate;
    private final RedisTemplate<String, Integer> integerRedisTemplate;
    public static final String EVALUATION_PREFIX = "eval_";
    public static final String NOT_EXIST = "false";
    public static final String HOBBY_PREFIX = "hobby_";
    public static final String MBTI_PREFIX = "mbti_";
    public static final String LIFESTYLE_PREFIX = "lifestyle_";
    public static final String INCOME_PREFIX = "income_";
    public static final String MATCHING_PREFIX = "matching_";
    public static final String PAID_MATCHING_PREFIX = "paidMatching_";
    private static final Duration EVALUATION_DURATION = Duration.ofDays(1);
    public static final Duration MATCHING_DURATION = Duration.ofDays(1);

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
            if (count == 4) {
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

    @Transactional(readOnly = true)
    public List<String> scanKeysWithPrefix(String prefix) {
        List<String> keys = new ArrayList<>();

        Cursor<byte[]> cursor = Objects.requireNonNull(strRedisTemplate.getConnectionFactory())
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

    public boolean isExistInt(String key) {
        return Boolean.TRUE.equals(integerRedisTemplate.hasKey(key));
    }

    public void delete(String key) {
        strRedisTemplate.delete(key);
    }

    public void setIntValue(String key, Integer data, Duration duration) {
        ValueOperations<String, Integer> values = integerRedisTemplate.opsForValue();
        values.set(key, data, duration);
    }
}
