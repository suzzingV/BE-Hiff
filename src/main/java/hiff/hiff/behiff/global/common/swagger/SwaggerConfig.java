package hiff.hiff.behiff.global.common.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "Hiff API 명세서",
        description = "Hiff API 명세서",
        version = "0"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi v0Api() {
        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
        String[] paths = {"/api/v0/**"};

        return GroupedOpenApi.builder()
            .group("Hiff API v0")  // 그룹 이름을 설정한다.
            .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
            .build();
    }

//    @Bean
//    public GroupedOpenApi userApi() {
//        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
//        String[] paths = {"/v1/user"};
//
//        return GroupedOpenApi.builder()
//                .group("촌스러운 여행 user API v1")  // 그룹 이름을 설정한다.
//                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi authApi() {
//        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
//        String[] paths = {"/v1/auth"};
//
//        return GroupedOpenApi.builder()
//                .group("촌스러운 여행 auth API v1")  // 그룹 이름을 설정한다.
//                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi festivalApi() {
//        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
//        String[] paths = {"/v1/festival"};
//
//        return GroupedOpenApi.builder()
//                .group("촌스러운 여행 festival API v1")  // 그룹 이름을 설정한다.
//                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi houseApi() {
//        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
//        String[] paths = {"/v1/house"};
//
//        return GroupedOpenApi.builder()
//                .group("촌스러운 여행 house API v1")  // 그룹 이름을 설정한다.
//                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi likeApi() {
//        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
//        String[] paths = {"/v1/like"};
//
//        return GroupedOpenApi.builder()
//                .group("촌스러운 여행 like API v1")  // 그룹 이름을 설정한다.
//                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi reviewApi() {
//        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
//        String[] paths = {"/v1/house"};
//
//        return GroupedOpenApi.builder()
//                .group("촌스러운 여행 review API v1")  // 그룹 이름을 설정한다.
//                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
//                .build();
//    }
}
