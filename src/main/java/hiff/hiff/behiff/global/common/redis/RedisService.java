package hiff.hiff.behiff.global.common.redis;

import static hiff.hiff.behiff.domain.evaluation.application.EvaluationService.EVALUATION_DURATION;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    public static final String NOT_EXIST = "false";


    public void setValue(String key, Object data, Duration duration) {
        String value = String.valueOf(data);
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }

    public void setValue(String key, Object data) {
        String value = String.valueOf(data);
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value);
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

    public Set<String> keys(String prefix) {
        return redisTemplate.keys(prefix);
    }
}
