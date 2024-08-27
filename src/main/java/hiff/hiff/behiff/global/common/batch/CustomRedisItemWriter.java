package hiff.hiff.behiff.global.common.batch;

import org.springframework.batch.item.KeyValueItemWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class CustomRedisItemWriter<T, K> extends KeyValueItemWriter<T, K> {

    private RedisTemplate<K, T> redisTemplate;

    @Override
    protected void writeKeyValue(T value, K key) {
        if (this.delete) {
            this.redisTemplate.delete(key);
        } else {
            this.redisTemplate.opsForValue().set(key, value);
        }
    }

    @Override
    protected void init() {
        Assert.notNull(this.redisTemplate, "RedisTemplate must not be null");
    }

    /**
     * Set the {@link RedisTemplate} to use.
     *
     * @param redisTemplate the template to use
     */
    public void setRedisTemplate(RedisTemplate<K, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
