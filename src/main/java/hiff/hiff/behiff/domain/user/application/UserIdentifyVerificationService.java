package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.common.sms.SmsService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserIdentifyVerificationService {

    private final SmsService smsService;
    private final RedisService redisService;

    private static final Duration IDENTIFY_VERIFICATION_DURATION = Duration.ofMinutes(5);

    public void sendIdentificationSms(User user, String phoneNum) {
        String realPhoneNum = phoneNum.replaceAll("-","");
        Random random = new Random();
        int number = random.nextInt(1000000);
        String verificationCode = String.format("%06d", number);

        smsService.sendVerificationCode(realPhoneNum, verificationCode);

        redisService.setValue(verificationCode, user.getId(), IDENTIFY_VERIFICATION_DURATION);
    }

    public void checkCode(Long userId, String code) {
        Long savedUserId = redisService.getLongValue(code);
        if(!userId.equals(savedUserId)) {
            throw new UserException(ErrorCode.VERIFICATION_CODE_INCORRECT);
        }
    }
}
