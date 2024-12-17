package hiff.hiff.behiff.global.common.batch.hiff_matching;

import hiff.hiff.behiff.domain.matching.application.dto.UserWithMatchCount;
import hiff.hiff.behiff.domain.matching.application.service.MatchingService;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.infrastructure.UserProfileRepository;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.profile.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.domain.entity.Device;
import hiff.hiff.behiff.global.auth.infrastructure.DeviceRepository;
import hiff.hiff.behiff.global.common.batch.CustomSkipListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Future;

import hiff.hiff.behiff.global.common.fcm.FcmUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class HiffMatchingBatchConfig {

    private final CustomSkipListener customSkipListener;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final HiffMatchingJobExecutionListener hiffMatchingJobExecutionListener;
    private final MatchedStepExecutionListener matchedStepExecutionListener;
//    private final HiffMatchingService hiffMatchingService;
    private final UserProfileRepository userProfileRepository;
    private final MatchingService matchingService;
    public static PriorityQueue<UserWithMatchCount> matchedQueue = new PriorityQueue<>();
    public static List<User> matchedList = new ArrayList<>();

//    @Bean
//    public Job hiffMatchingByMaleJob() {
//        return new JobBuilder("hiffMatchingByMaleJob", jobRepository)
//            .listener(hiffMatchingJobExecutionListener)
////            .start(getFemaleStep())
//            .start(matchingByMaleStep())
//            .build();
//    }

    @Bean
    public Job randomMatchingJob() {
        return new JobBuilder("randomMatchingJob", jobRepository)
                .listener(hiffMatchingJobExecutionListener)
                .start(randomMatchingStep())
                .build();
    }

    @Bean
    public Job matchingAlarmJob() {
        return new JobBuilder("matchingAlarmJob", jobRepository)
                .listener(hiffMatchingJobExecutionListener)
                .start(matchingAlarmStep())
                .build();
    }

//    @Bean
//    public Job hiffMatchingByFemaleJob() {
//        return new JobBuilder("hiffMatchingByFemaleJob", jobRepository)
//            .listener(hiffMatchingJobExecutionListener)
////            .start(getMaleStep())
//            .start(matchingByFemaleStep())
//            .build();
//    }

//    @Bean
//    public Step getMaleStep() {
//        return new StepBuilder("getMaleStep", jobRepository)
//            .<User, Future<User>>chunk(5000, transactionManager)
//            .reader(maleReader())
//            .processor(matchedAsyncItemProcessor())
//            .writer(matchedAsyncItemWriter()) // 비동기로 실행하기 위한 TaskExecutor 설정
//            .listener(matchedStepExecutionListener)
//            .faultTolerant()
//            .skip(Exception.class)
//            .listener(customSkipListener)
//            .retry(Exception.class)
//            .retryLimit(3)
//            .build();
//    }

//    @Bean
//    public Step getFemaleStep() {
//        return new StepBuilder("getFemaleStep", jobRepository)
//            .<User, Future<User>>chunk(5000, transactionManager)
//            .reader(femaleReader())
//            .processor(matchedAsyncItemProcessor())
//            .writer(matchedAsyncItemWriter()) // 비동기로 실행하기 위한 TaskExecutor 설정
//            .listener(matchedStepExecutionListener)
//            .faultTolerant()
//            .skip(Exception.class)
//            .listener(customSkipListener)
//            .retry(Exception.class)
//            .retryLimit(3)
//            .build();
//    }

//    @Bean
//    public Step matchingByMaleStep() {
//        return new StepBuilder("matchingByMaleStep", jobRepository)
//            .<UserProfile, Future<UserProfile>>chunk(5000, transactionManager)
//            .reader(userProfileReader())
//            .processor(matcherAsyncItemProcessor())
//            .writer(matcherAsyncItemWriter())
//            .faultTolerant()
//            .skip(Exception.class)
//            .listener(customSkipListener)
//            .retry(Exception.class)
//            .retryLimit(3)
//            .build();
//    }

    @Bean
    public Step randomMatchingStep() {
        return new StepBuilder("randomMatchingStep", jobRepository)
                .<UserProfile, Future<UserProfile>>chunk(5000, transactionManager)
                .reader(userProfileReader())
                .processor(matcherAsyncItemProcessor())
                .writer(matcherAsyncItemWriter())
                .faultTolerant()
                .skip(Exception.class)
                .listener(customSkipListener)
                .retry(Exception.class)
                .retryLimit(3)
                .build();
    }

    @Bean
    public Step matchingAlarmStep() {
        return new StepBuilder("matchingAlarmStep", jobRepository)
                .<Device, Future<Device>>chunk(5000, transactionManager)
                .reader(deviceReader())
                .processor(deviceAsyncItemProcessor())
                .writer(deviceAsyncItemWriter())
                .faultTolerant()
                .skip(Exception.class)
                .listener(customSkipListener)
                .retry(Exception.class)
                .retryLimit(3)
                .build();
    }

//    @Bean
//    public Step matchingByFemaleStep() {
//        return new StepBuilder("matchingByFemaleStep", jobRepository)
//            .<User, Future<User>>chunk(5000, transactionManager)
//            .reader(femaleReader())
//            .processor(matcherAsyncItemProcessor())
//            .writer(matcherAsyncItemWriter())
//            .faultTolerant()
//            .skip(Exception.class)
//            .listener(customSkipListener)
//            .retry(Exception.class)
//            .retryLimit(3)
//            .build();
//    }

//    @Bean
//    public RepositoryItemReader<UserProfile> maleReader() {
//        RepositoryItemReader<UserProfile> reader = new RepositoryItemReader<>();
//        reader.setRepository(userProfileRepository);
//        reader.setMethodName("findByGender");
//        reader.setArguments(Collections.singletonList(Gender.MALE));
//        reader.setPageSize(5000);
//        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
//        return reader;
//    }

    @Bean
    public RepositoryItemReader<UserProfile> userProfileReader() {
        RepositoryItemReader<UserProfile> reader = new RepositoryItemReader<>();
        reader.setRepository(userProfileRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(5000);
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public RepositoryItemReader<Device> deviceReader() {
        RepositoryItemReader<Device> reader = new RepositoryItemReader<>();
        reader.setRepository(deviceRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(5000);
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

//    @Bean
//    public RepositoryItemReader<User> femaleReader() {
//        RepositoryItemReader<User> reader = new RepositoryItemReader<>();
//        reader.setRepository(userRepository);
//        reader.setMethodName("findByGender");
//        reader.setArguments(Collections.singletonList(Gender.FEMALE));
//        reader.setPageSize(5000);  // 페이지당 읽을 데이터 개수
//        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
//        return reader;
//    }

    @Bean
    public AsyncItemProcessor<UserProfile, UserProfile> matcherAsyncItemProcessor() {
        AsyncItemProcessor<UserProfile, UserProfile> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setTaskExecutor(taskExecutor()); // 스레드풀 지정
        asyncItemProcessor.setDelegate(matcherItemProcessor());
        return asyncItemProcessor;
    }

    @Bean
    public AsyncItemProcessor<Device, Device> deviceAsyncItemProcessor() {
        AsyncItemProcessor<Device, Device> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setTaskExecutor(taskExecutor()); // 스레드풀 지정
        asyncItemProcessor.setDelegate(deviceItemProcessor());
        return asyncItemProcessor;
    }

    @Bean
    public AsyncItemProcessor<User, User> matchedAsyncItemProcessor() {
        AsyncItemProcessor<User, User> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setTaskExecutor(taskExecutor()); // 스레드풀 지정
        asyncItemProcessor.setDelegate(matchedItemProcessor());
        return asyncItemProcessor;
    }

    @Bean
    public ItemProcessor<UserProfile, UserProfile> matcherItemProcessor() {
        return new ItemProcessor<UserProfile, UserProfile>() {
            @Override
            public UserProfile process(UserProfile matcher) {
//                PriorityQueue<UserWithMatchCount> matchedArr = new PriorityQueue<>(matchedQueue);
//                hiffMatchingService.dailyMatching(matcher, matchedArr);
                matchingService.performMatching(matcher);
                return null;
            }
        };
    }

    @Bean
    public ItemProcessor<Device, Device> deviceItemProcessor() {
        return new ItemProcessor<Device, Device>() {
            @Override
            public Device process(Device device) {
                FcmUtils.sendMatchingAlarm(device.getFcmToken(), device.getUserId());
                return null;
            }
        };
    }

    @Bean
    public ItemProcessor<User, User> matchedItemProcessor() {
        return matched -> {
            matchedList.add(matched);
            return null;
        };
    }

    @Bean
    public AsyncItemWriter<UserProfile> matcherAsyncItemWriter() {
        AsyncItemWriter<UserProfile> writer = new AsyncItemWriter<>();
        writer.setDelegate(matcherWriter());
        return writer;
    }

    @Bean
    public AsyncItemWriter<Device> deviceAsyncItemWriter() {
        AsyncItemWriter<Device> writer = new AsyncItemWriter<>();
        writer.setDelegate(deviceWriter());
        return writer;
    }

    @Bean
    public AsyncItemWriter<User> matchedAsyncItemWriter() {
        AsyncItemWriter<User> writer = new AsyncItemWriter<>();
        writer.setDelegate(matchedWriter());
        return writer;
    }

    @Bean
    public ItemWriter<UserProfile> matcherWriter() {
        return new ItemWriter<UserProfile>() {
            @Override
            public void write(Chunk<? extends UserProfile> chunk) throws Exception {
            }
        };
    }

    @Bean
    public ItemWriter<Device> deviceWriter() {
        return new ItemWriter<Device>() {
            @Override
            public void write(Chunk<? extends Device> chunk) throws Exception {
            }
        };
    }

    @Bean
    public ItemWriter<User> matchedWriter() {
        return new ItemWriter<User>() {
            @Override
            public void write(Chunk<? extends User> chunk) throws Exception {
            }
        };
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(12); // 스레드 풀의 코어 크기 설정
        taskExecutor.setMaxPoolSize(24); // 스레드 풀의 최대 크기 설정
        taskExecutor.setQueueCapacity(600); // 큐의 용량 설정
        taskExecutor.afterPropertiesSet();
        taskExecutor.setRejectedExecutionHandler(new BlockingRejectedExecutionHandler());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
