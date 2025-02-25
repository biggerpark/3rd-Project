package com.green.jobdone.user;

import com.green.jobdone.common.CookieUtils;
import com.green.jobdone.common.model.ResultResponse;
import com.green.jobdone.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "유저")
public class UserController {
    private final UserService service;




    @PostMapping(value = "sign-up")
        @Operation(summary = "회원가입")
        public ResultResponse<Integer> postUserSignUp(@Valid @RequestPart UserSignUpReq p,  @RequestPart(required = false) MultipartFile pic) {
            int result = service.postUserSignUp(p, pic);

            return ResultResponse.<Integer>builder()
                    .resultMessage("회원가입완료")
                .resultData(result)
                .build();
    }


    @PostMapping("sign-in")
    @Operation(summary = "로그인")
    public ResultResponse<UserSignInRes> postUserSignIn(@RequestBody UserSignInReq p, HttpServletResponse response) {
        UserSignInRes res = service.postUserSignIn(p, response);
        return ResultResponse.<UserSignInRes>builder()
                .resultMessage(res.getMessage())
                .resultData(res)
                .build();
    }
    @PostMapping("sign-in test business")
    @Operation(summary = "로그인")
    public ResultResponse<UserSignInRes> postUserSignInBusiness(@RequestBody UserTestReq p, HttpServletResponse response) {
        UserSignInReq req = new UserSignInReq();
        req.setEmail(p.getEmail());
        req.setUpw(p.getUpw());
        UserSignInRes res = service.postUserSignIn(req, response);
        return ResultResponse.<UserSignInRes>builder()
                .resultMessage(res.getMessage())
                .resultData(res)
                .build();
    }


    @PostMapping("email")
    @Operation(summary = "이메일 중복 체크")
    public ResultResponse<Integer> postUserEmailCheck(@RequestBody UserEmailCheckReq p) {
        int result = service.postUserEmailCheck(p.getEmail());

        return ResultResponse.<Integer>builder()
                .resultData(result)
                .resultMessage(result == 0 ? "존재하는 이메일 입니다" : "이메일 인증해주세요")
                .build();

    }


    @GetMapping
    @Operation(summary = "회원정보 조회")
    public ResultResponse<UserInfoGetRes> getUserInfo(){
        UserInfoGetRes res= service.getUserInfo();

        return ResultResponse.<UserInfoGetRes>builder()
                .resultMessage(res==null?"회원정보가 없습니다":"회원 정보 조회 완료")
                .resultData(res)
                .build();
    }

    @PostMapping("access-token")
    @Operation(summary = "accessToken 재발행")
    public ResultResponse<String> postUserAccessToken(HttpServletRequest req) {


       String accessToken=service.postUserAccessToken(req);


        return ResultResponse.<String>builder()
                .resultMessage(accessToken==null?"유효하지 않은 refreshToken 입니다":"토큰 갱신 성공")
                .resultData(accessToken)
                .build();
    }

   @PatchMapping
   @Operation(summary = "회원정보 수정")
   public ResultResponse<Integer> updateUserInfo(@RequestPart UserInfoPatchReq p, @RequestPart(required = false) MultipartFile pic) {


        int result=service.updateUserInfo(p,pic);



       return ResultResponse.<Integer>builder()
               .resultMessage(result==0?"회원정보 수정사항 없음":"회원정보 수정 완료")
               .resultData(result)
               .build();
   }

   @DeleteMapping
   @Operation(summary = "회원정보 삭제")
    public ResultResponse<Integer> deleteUser(@RequestBody UserInfoDelReq p) {

        int result=service.deleteUser(p);
        log.info("p:{}",p);


       return ResultResponse.<Integer>builder()
               .resultMessage(result==0?"비밀번호가 일치하지 않습니다":"회원정보 삭제 완료")
               .resultData(result)
               .build();


   }


   @PatchMapping("password")
   @Operation(summary = "비밀번호 변경")
   public ResultResponse<Integer> updatePassword(@RequestBody UserPwPatchReq p) {

       UserPwPatchRes result=service.updatePassword(p);
       log.info("p:{}",p);


       return ResultResponse.<Integer>builder()
               .resultMessage(result.getMessage())
               .resultData(result.getResult())
               .build();


   }




}

