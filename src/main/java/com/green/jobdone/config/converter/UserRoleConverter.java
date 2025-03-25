package com.green.jobdone.config.converter;

import com.green.jobdone.config.jwt.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

// UserRole enum 을 db에 저장시 code로 저장하고 싶어 설정된 converter 객체
@Converter(autoApply = true)  // 모든 UserRole 필드에 자동 적용
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRole userRole) {
        if (userRole == null) {
            return null;
        }
        return userRole.getCode();  // Enum의 코드 값을 DB에 저장
    }

    @Override
    public UserRole convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return Stream.of(UserRole.values())
                .filter(role -> role.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid UserRole code: " + code));
    }
}