package hiff.hiff.behiff.global.common.batch.matching_init;

import hiff.hiff.behiff.global.common.batch.CustomSkipListener;
import hiff.hiff.behiff.global.util.DateCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static hiff.hiff.behiff.domain.matching.application.service.MatchingService.MATCHING_PREFIX;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MatchingInitBatchConfig {

    private final CustomSkipListener customSkipListener;
    private final JobRepository jobRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PlatformTransactionManager transactionManager;
    private final DefaultJobExecutionListener defaultJobExecutionListener;
    private final Converter<String, String> itemKeyMapper = new Converter<String, String>() {
        @Override
        public String convert(String key) {
            return redisTemplate.opsForValue().get(key);
        }
    };

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
        String yesterdayDate = DateCalculator.getYesterdayDate();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(MATCHING_PREFIX + yesterdayDate + "*")
            .count(1000).build();
        return new CustomRedisItemReader<>(redisTemplate, scanOptions);
    }

    @Bean
    public CustomRedisItemWriter<String, String> redisItemWriter() {
        CustomRedisItemWriter<String, String> customRedisItemWriter = new CustomRedisItemWriter<>();
        customRedisItemWriter.setRedisTemplate(redisTemplate);
        customRedisItemWriter.setDelete(true);
        customRedisItemWriter.setItemKeyMapper(itemKeyMapper);
        return customRedisItemWriter;
    }
}
