package hiff.hiff.behiff.global.common.batch;

import static hiff.hiff.behiff.global.common.redis.RedisService.DAILY_MATCHING_PREFIX;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final CustomSkipListener customSkipListener;
    private final JobRepository jobRepository;
    private final RedisTemplate<String, String> strRedisTemplate;
    private final PlatformTransactionManager transactionManager;
    private final UserRepository userRepository;
    private final GenderReadJobExecutionListener genderReadJobExecutionListener;
    private final DefaultJobExecutionListener defaultJobExecutionListener;

    @Bean
    public Job dailyMatchingInitJob() {
        return new JobBuilder("dailyMatchingInitJob", jobRepository)
            .listener(defaultJobExecutionListener)
            .start(dailyMatchingDeleteStep())
            .build();
    }

    @Bean
    public Job getUserByGenderJob() {
        return new JobBuilder("getUserByGenderJob", jobRepository)
            .listener(genderReadJobExecutionListener)
            .start(getMaleStep())
            .next(getFemaleStep())
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
    public Step getMaleStep() {
        return new StepBuilder("getMaleStep", jobRepository)
            .<User, User>chunk(1000, transactionManager)
            .reader(maleReader())
            .writer(maleWriter())
            .faultTolerant()
            .skip(Exception.class)
            .listener(customSkipListener)
            .retry(Exception.class)
            .retryLimit(3)
            .build();
    }

    @Bean
    public Step getFemaleStep() {
        return new StepBuilder("getFemaleStep", jobRepository)
            .<User, User>chunk(1000, transactionManager)
            .reader(femaleReader())
            .writer(femaleWriter())
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
    public RepositoryItemReader<User> maleReader() {
        RepositoryItemReader<User> reader = new RepositoryItemReader<>();
        reader.setRepository(userRepository);
        reader.setMethodName("findByGender");
        reader.setArguments(Collections.singletonList(Gender.MALE));
        reader.setPageSize(1000);  // 페이지당 읽을 데이터 개수
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public ItemWriter<User> maleWriter() {
        return chunk -> genderReadJobExecutionListener.addMales(chunk.getItems());
    }

    @Bean
    public RepositoryItemReader<User> femaleReader() {
        RepositoryItemReader<User> reader = new RepositoryItemReader<>();
        reader.setRepository(userRepository);
        reader.setMethodName("findByGender");
        reader.setArguments(Collections.singletonList(Gender.FEMALE));
        reader.setPageSize(1000);  // 페이지당 읽을 데이터 개수
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public ItemWriter<User> femaleWriter() {
        return chunk -> genderReadJobExecutionListener.addFemales(chunk.getItems());
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
