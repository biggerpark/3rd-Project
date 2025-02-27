package com.green.jobdone.config.jwt;

public enum UserRole {
    ADMIN(101),
    USER(100),
    EMPLOYEE(110),
    MANAGER(120),
    PRESIDENT(130),
    FREELANCER(140),
    EXA(999);

    private final int code;

    UserRole(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static UserRole fromCode(int code) {
        for (UserRole role : UserRole.values()) {
            if (role.getCode() == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role code: " + code);
    }


}
