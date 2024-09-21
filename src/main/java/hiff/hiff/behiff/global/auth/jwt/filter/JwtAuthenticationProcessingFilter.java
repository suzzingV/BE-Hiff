package hiff.hiff.behiff.global.auth.jwt.filter;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.StringTokenizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        checkLogout(request); //로그아웃한 사용자면 인증 처리 안함

        jwtService.extractAccessToken(request)
            .ifPresent(accessToken -> {
                if (!jwtService.isTokenValid(accessToken)) { //accessToken 만료 시
                    throw new AuthException(ErrorCode.SECURITY_INVALID_TOKEN);
                }
            });
        checkAccessTokenAndSaveAuthentication(request, response, filterChain);
    }

    private void checkLogout(HttpServletRequest request) {
        jwtService.extractAccessToken(request).ifPresent(accessToken -> {
            String value = redisService.getStrValue(accessToken);
            if (value.equals("logout")) {
                throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
            }
        });
    }

    private void checkAccessTokenAndSaveAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain) {
        jwtService.extractAccessToken(request)
            .flatMap(jwtService::extractSocialInfo)
            .flatMap(socialInfo -> {
                StringTokenizer st = new StringTokenizer(socialInfo, " ");
                String prefix = st.nextToken();
                return userRepository.findBySocialTypeAndSocialId(SocialType.getSocialTypeFromPrefix(prefix), st.nextToken());
            }).ifPresent(this::saveAuthentication);

        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new AuthException(ErrorCode.SERVER_ERROR);
        }
    }

    private void saveAuthentication(User myUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(myUser, null,
            authoritiesMapper.mapAuthorities(myUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
