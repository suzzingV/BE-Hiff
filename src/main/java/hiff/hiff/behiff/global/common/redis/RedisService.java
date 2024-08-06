package hiff.hiff.behiff.global.common.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Transactional
public class RedisService {

    private final RedisTemplate<String, String> strRedisTemplate;
    private final RedisTemplate<String, Integer> integerRedisTemplate;
    private static final String EVALUATION_PREFIX = "eval_";
    private static final Duration EVALUATION_DURATION = Duration.ofDays(1);

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = strRedisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public void updateEvaluationValues(String userId) {
        ValueOperations<String, Integer> values = integerRedisTemplate.opsForValue();
        String key = EVALUATION_PREFIX + userId;
        if(getEvaluationValues(key) == 0) {
            values.set(key, 1);
        } else {
            int count = getEvaluationValues(key);
            if(count >= 4) {
                values.set(key, 5, EVALUATION_DURATION);
            } else {
                values.set(key, count + 1);
            }
        }
    }

    public boolean isEvaluationAvailable(String userId) {
        String key = EVALUATION_PREFIX + userId;
        return !(getEvaluationValues(key) == 5);
    }

    @Transactional(readOnly = true)
    public Integer getEvaluationValues(String key) {
        ValueOperations<String, Integer> values = integerRedisTemplate.opsForValue();
        if (values.get(key) == null) {
            return 0;
        }
        return values.get(key);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, String> values = strRedisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return values.get(key);
    }

    public void delete(String key) {
        strRedisTemplate.delete(key);
    }

    protected boolean checkExistsValue(String value) {
        return !value.equals("false");
    }
}
