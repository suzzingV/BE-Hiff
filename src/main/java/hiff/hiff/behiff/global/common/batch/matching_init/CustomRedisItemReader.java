package hiff.hiff.behiff.global.common.batch.matching_init;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.Assert;

public class CustomRedisItemReader<K, V> implements ItemStreamReader<K> {

    public static class KeyValue<K, V> {

        K key;
        V value;

        KeyValue(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final RedisTemplate<K, V> redisTemplate;

    private final ScanOptions scanOptions;

    private Cursor<K> cursor;

    public CustomRedisItemReader(RedisTemplate<K, V> redisTemplate, ScanOptions scanOptions) {
        Assert.notNull(redisTemplate, "redisTemplate must not be null");
        Assert.notNull(scanOptions, "scanOptions must no be null");
        this.redisTemplate = redisTemplate;
        this.scanOptions = scanOptions;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.cursor = this.redisTemplate.scan(this.scanOptions);
    }

    @Override
    public K read() throws Exception {
        if (this.cursor.hasNext()) {
            K next = this.cursor.next();
            return next;
        } else {
            return null;
        }
    }

    @Override
    public void close() throws ItemStreamException {
        this.cursor.close();
    }
}
