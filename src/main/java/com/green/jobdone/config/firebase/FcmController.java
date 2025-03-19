package com.green.jobdone.config.firebase;

import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.User;
import com.green.jobdone.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("savePushToken")
@RequiredArgsConstructor
public class FcmController {
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;



    @PostMapping
    public String saveFcmToken(@RequestBody String token) {
        Long userId = authenticationFacade.getSignedUserId();
        User user = userRepository.findById(userId).orElse(null);
        user.setFCMToken(token);
        userRepository.save(user);
        return "저장 완료";
    }
}
