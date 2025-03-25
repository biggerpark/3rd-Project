package com.green.jobdone.user.model;

import com.green.jobdone.config.jwt.UserRole;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UserRole role;
    private Long userId;
    private String email;
}
