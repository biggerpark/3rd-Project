package com.green.jobdone.config.firebase;

import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.User;
import com.green.jobdone.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("savePushToken")
@RequiredArgsConstructor
@Slf4j
public class FcmController {
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;



    @PostMapping
    public String saveFcmToken(@RequestBody String token) {
        log.info("Token 토큰 저장하는곳 잘 들어왔냐??: " + token);
        Long userId = authenticationFacade.getSignedUserId();
        log.info("userId: " + userId);
        User user = userRepository.findById(userId).orElse(null);
        log.info("user: " + user);
        user.setFCMToken(token);
        userRepository.save(user);
        log.info("저장 잘됬음?? 어케됨??");
        return "저장 완료";
    }
}
