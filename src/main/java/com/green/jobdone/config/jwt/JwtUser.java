package com.green.jobdone.config.jwt;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@EqualsAndHashCode //Equals, HashCode 메소드 오버라이딩
public class JwtUser {
    private long signedUserId;
    private List<UserRole> roles; //인가(권한)처리 때 사용, ROLE_이름, ROLE_USER, ROLE_ADMIN
}
