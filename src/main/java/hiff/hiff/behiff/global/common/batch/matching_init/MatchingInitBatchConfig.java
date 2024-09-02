package hiff.hiff.behiff.global.common.batch.matching_init;

import static hiff.hiff.behiff.global.common.redis.RedisService.DAILY_MATCHING_PREFIX;

import hiff.hiff.behiff.global.common.batch.CustomSkipListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
// TODO: 푸시 알림
public class MatchingInitBatchConfig {

    private final CustomSkipListener customSkipListener;
    private final JobRepository jobRepository;
    private final RedisTemplate<String, String> strRedisTemplate;
    private final PlatformTransactionManager transactionManager;
    private final DefaultJobExecutionListener defaultJobExecutionListener;

    @Bean
    public Job dailyMatchingInitJob() {
        return new JobBuilder("dailyMatchingInitJob", jobRepository)
            .listener(defaultJobExecutionListener)
            .start(dailyMatchingDeleteStep())
            .build();
    }

    @Bean
    public Step dailyMatchingDeleteStep() {
        return new StepBuilder("dailyMatchingDeleteStep", jobRepository)
            .<String, String>chunk(1000, transactionManager)
            .reader(redisItemReader())
            .writer(redisItemWriter())
            .faultTolerant()
            .skip(Exception.class)
            .listener(customSkipListener)
            .retry(Exception.class)
            .retryLimit(3)
            .build();
    }


    @Bean
    public CustomRedisItemReader<String, String> redisItemReader() {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(DAILY_MATCHING_PREFIX + "*")
            .count(1000).build();
        return new CustomRedisItemReader<>(strRedisTemplate, scanOptions);
    }

    @Bean
    public CustomRedisItemWriter<String, String> redisItemWriter() {
        CustomRedisItemWriter<String, String> customRedisItemWriter = new CustomRedisItemWriter<>();
        customRedisItemWriter.setRedisTemplate(strRedisTemplate);
        customRedisItemWriter.setDelete(true);
        customRedisItemWriter.setItemKeyMapper(itemKeyMapper());
        return customRedisItemWriter;
    }

    @Bean
    public Converter<String, String> itemKeyMapper() {
        return new Converter<String, String>() {
            @Override
            public String convert(String key) {
                return strRedisTemplate.opsForValue().get(key);
            }
        };
    }
}
