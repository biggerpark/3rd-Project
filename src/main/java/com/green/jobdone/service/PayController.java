package com.green.jobdone.service;

import com.green.jobdone.service.model.Dto.KakaoPayReq;
import com.green.jobdone.service.model.Dto.SessionUtil;
import com.green.jobdone.service.model.KakaoPayRedayRes;
import com.green.jobdone.service.model.KakaoPayRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
@Slf4j
@Tag(name = "카카오페이api")
public class PayController {
    private final PayService payService;


    @PostMapping("/ready")
    @Operation(summary = "결제 요청 보내기")
    public KakaoPayRedayRes startKakaoPay(@RequestParam Long serviceId, HttpSession session ) {
        log.info("serviceId : {}", serviceId);
        KakaoPayRedayRes a = payService.useKakaoPay(serviceId);
//        HttpSession session = request.getSession(true);
        session.setAttribute("tid", a.getTid());
        session.setAttribute("serviceId", serviceId);
//        SessionUtil.addAttribute("tid", a.getTid());
//        SessionUtil.addAttribute("serviceId", serviceId);
        log.info("Session tid: {}", session.getAttribute("tid"));
        log.info("Session serviceId: {}", session.getAttribute("serviceId"));

        return a;
    }

    @GetMapping("/success")
    @Operation(summary = "결제 성공 후 완료 메세지 보내기")
    public ResponseEntity<KakaoPayRes> paySuccess(@RequestParam("pg_token") String pgToken, HttpSession session) {
        log.info("pgToken: {} ",pgToken);


        KakaoPayRes kakaoPayRes = payService.payRes(pgToken, session);


        return new ResponseEntity<>(kakaoPayRes, HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel() {
        return new ResponseEntity<>("Payment cancelled.", HttpStatus.OK);
    }

    @GetMapping("/fail")
    public ResponseEntity<String> fail() {
        return new ResponseEntity<>("Payment failed.", HttpStatus.OK);
    }
}
