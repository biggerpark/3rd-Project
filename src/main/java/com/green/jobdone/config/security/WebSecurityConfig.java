package com.green.jobdone.config.security;

//Spring Security 세팅
import com.green.jobdone.config.handler.CustomAccessDeniedHandler;
import com.green.jobdone.config.jwt.JwtAuthenticationEntryPoint;
import com.green.jobdone.config.jwt.TokenAuthenticationFilter;
import com.green.jobdone.config.jwt.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //메소드 빈등록이 있어야 의미가 있다. 메소드 빈등록이 싱글톤이 됨.
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    //스프링 시큐리티 기능 비활성화 (스프링 시큐리티가 관여하지 않았으면 하는 부분)
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring()
//                        .requestMatchers(new AntPathRequestMatcher("/static/**"));
//    }


    @Bean //스프링이 메소드 호출을 하고 리턴한 객체의 주소값을 관리한다. (빈 등록)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) //세션 필요할때만 사용
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 사용안함
                .httpBasic(h -> h.disable()) // SSR을 사용하지 않기 때문에 HTTP Basic 인증 비활성화
                .formLogin(form -> form.disable()) // SSR을 사용하지 않기 때문에 폼 로그인 비활성화
                .csrf(csrf -> csrf.disable()) // SSR이 아니면 CSRF 보호 필요 없음
                .authorizeHttpRequests(req -> req
                                //유저쪽
                                .requestMatchers(HttpMethod.GET,"/api/user").authenticated()
                                .requestMatchers(HttpMethod.PATCH,"/api/user").authenticated()   //   나중에 대충 다 만들면 api 인증받을거 처리하기
                                .requestMatchers(HttpMethod.DELETE,"/api/user").authenticated()
                                //리뷰,리뷰댓글쪽
                                .requestMatchers(HttpMethod.GET,"/api/review/**").permitAll() // 댓글 get 은 인증된 사용자가 아니어도 볼 수 있음
                                .requestMatchers("/api/review/**").authenticated()
                                //서비스쪽
                                .requestMatchers(HttpMethod.GET,"/api/service/**").permitAll()
                                .requestMatchers("/api/service/**").authenticated()
                                //업체쪽
                                .requestMatchers(HttpMethod.GET,"/api/business/**").permitAll() // 댓글 get 은 인증된 사용자가 아니어도 볼 수 있음
                                .requestMatchers(HttpMethod.POST,"/api/business/sign-up").authenticated() // 로그인된 사용자는 업체 등록 가능해야함.
                                .requestMatchers("/api/business/**").hasAnyRole(
                                        UserRole.EMPLOYEE.name(),
                                        UserRole.MANAGER.name(),
                                        UserRole.PRESIDENT.name(),
                                        UserRole.FREELANCER.name()
                                )
                                //상품쪽
                                .requestMatchers(HttpMethod.GET,"/api/product/**").permitAll()
                                .requestMatchers("/api/product/**").hasAnyRole(
                                        UserRole.EMPLOYEE.name(),
                                        UserRole.MANAGER.name(),
                                        UserRole.PRESIDENT.name(),
                                        UserRole.FREELANCER.name()
                                )
                                //문의관련쪽
                                .requestMatchers("/api/room").authenticated()
                                //문의채팅쪽
                                .requestMatchers("/api/chat").authenticated()
                                //찜쪽
                                .requestMatchers("/api/like").authenticated()
                                //카테고리쪽
                                .requestMatchers(HttpMethod.GET,"/api/category/**").permitAll()
                                .requestMatchers("/api/category").authenticated()

                                //포트폴리오쪽
                                .requestMatchers(HttpMethod.GET,"/api/portfolio/**").permitAll()
                                .requestMatchers("/api/portfolio/**").hasAnyRole(
                                        UserRole.EMPLOYEE.name(),
                                        UserRole.MANAGER.name(),
                                        UserRole.PRESIDENT.name(),
                                        UserRole.FREELANCER.name()
                                )
                                //카카오페이쪽
                                .requestMatchers("/api/payment/**").permitAll()

                                //스웨거쪽
                                .requestMatchers("/chat/**").permitAll()
                                .requestMatchers("/api/swagger-ui/**", "/api/v3/api-docs/**").permitAll()
//                              .requestMatchers("/api/like").hasRole(UserRole.USER.name()) // /api/like는 USER 역할을 가진 사용자만 접근 가능
                                .anyRequest().permitAll() // 그 외의 모든 요청은 허용
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)// 인증 실패 시 jwtAuthenticationEntryPoint 처리, 401 Unauthorized 처리(토큰이 필요하다는 에러)
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))  // 403 Forbidden 처리(접근 가능한 role 이 아니다 라는 에러)
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // JWT 인증 필터 추가
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}