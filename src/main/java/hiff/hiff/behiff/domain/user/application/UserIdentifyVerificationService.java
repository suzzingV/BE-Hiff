package hiff.hiff.behiff.domain.user.application;

import static hiff.hiff.behiff.domain.user.util.VerificationCodeGenerator.getCode;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.common.sms.SmsUtil;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserIdentifyVerificationService {

    private final SmsUtil smsUtil;
    private final RedisService redisService;

    private static final Duration IDENTIFY_VERIFICATION_DURATION = Duration.ofMinutes(5);
    private static final String IDENTIFY_VERIFICATION_PREFIX = "verify_";

    public void sendIdentificationSms(User user, String phoneNum) {
        String verificationCode = getCode();

        smsUtil.sendVerificationCode(phoneNum, verificationCode);
        redisService.setValue(IDENTIFY_VERIFICATION_PREFIX + verificationCode, user.getId(),
            IDENTIFY_VERIFICATION_DURATION);
    }

    public void checkCode(Long userId, String code) {
        Long savedUserId = redisService.getLongValue(IDENTIFY_VERIFICATION_PREFIX + code);
        if (!userId.equals(savedUserId)) {
            throw new UserException(ErrorCode.VERIFICATION_CODE_INCORRECT);
        }
    }
}
