package com.green.jobdone.service;

import com.green.jobdone.service.model.Dto.KakaoPayReq;
import com.green.jobdone.service.model.KakaoPayCancelRes;
import com.green.jobdone.service.model.KakaoPayRedayRes;
import com.green.jobdone.service.model.KakaoPayRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
@Slf4j
@Tag(name = "카카오페이api")
public class PayController {
    private final PayService payService;


    @PostMapping("/ready")
    @Operation(summary = "결제 요청 보내기")
    public KakaoPayRedayRes startKakaoPay(@RequestParam Long serviceId) {
        log.info("serviceId : {}", serviceId);
        KakaoPayRedayRes a = payService.useKakaoPay(serviceId);
        String tid = a.getTid();
        payService.saveTid(serviceId,tid);
//        HttpSession session = request.getSession(true);
//        session.setAttribute("tid", a.getTid());
//        session.setAttribute("serviceId", serviceId);
//        Cookie tidCookie = new Cookie("tid", a.getTid());
//        Cookie serviceIdCookie = new Cookie("serviceId", String.valueOf(serviceId));

        // 쿠키의 유효 경로 설정
//        tidCookie.setPath("/");
//        serviceIdCookie.setPath("/");

        // 쿠키 만료 시간 설정 (선택 사항)
//        tidCookie.setMaxAge(60 * 60);  // 1시간
//        serviceIdCookie.setMaxAge(60 * 60);  // 1시간

        // 쿠키를 응답에 추가
//        response.addCookie(tidCookie);
//        response.addCookie(serviceIdCookie);

//        SessionUtil.addAttribute("tid", a.getTid());
//        SessionUtil.addAttribute("serviceId", serviceId);
//        log.info("Session tid: {}", session.getAttribute("tid"));
//        log.info("Session serviceId: {}", session.getAttribute("serviceId"));


        return a;
    }

    @GetMapping("/success")
    @Operation(summary = "결제 성공 후 완료 메세지 보내기")
    public RedirectView paySuccess(@RequestParam("pg_token") String pgToken, @RequestParam("service_id") Long serviceId) {
        log.info("pgToken: {} ",pgToken);

        return payService.payRes(pgToken, serviceId);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel() {
        return new ResponseEntity<>("Payment cancelled.", HttpStatus.OK);
    }

    @GetMapping("/fail")
    public ResponseEntity<String> fail() {
        return new ResponseEntity<>("Payment failed.", HttpStatus.OK);
    }
    @PostMapping("/cancel_pay")
    public KakaoPayCancelRes cancelKakaoPay(@RequestParam Long serviceId) {
        KakaoPayCancelRes a = payService.cancelKakaoPay(serviceId);
        return a;
    }
}
