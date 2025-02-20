package com.green.jobdone.user;
import com.green.jobdone.config.security.SignInProviderType;
import com.green.jobdone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
//<연결할 엔터티, PK 타입>
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndProviderType(String email, SignInProviderType signInProviderType); //메소드 쿼리
}
