package com.green.jobdone.user;

import com.green.jobdone.config.security.SignInProviderType;
import com.green.jobdone.entity.User;
import com.green.jobdone.user.model.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

//<연결할 엔터티, PK 타입>
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndProviderType(String email, SignInProviderType signInProviderType); //메소드 쿼리

    @Query("SELECT u.name FROM User u WHERE u.userId = :userId")
    String getUserNameByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM User u where u.role = 999 and u.updatedAt < :sixMonthAgo ")
    void deleteByDays(@Param("sixMonthsAgo") LocalDateTime sixMonthsAgo);

    @Query("select new com.green.jobdone.user.model.UserDto(u.role, u.userId, u.email) from User u where u.email =:email")
    UserDto checkPostUser(@Param("email") String email);

//    @Modifying
//    @Transactional
//    @Query("UPDATE User u SET u.name = :#{#user.name},u.phone=:#{#user.phone}, u.pic = :#{#user.pic} WHERE u.userId = :#{#user.userId}")
//    int updateUserInfo(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :#{#user.name}, u.phone = :#{#user.phone}, " +
            "u.pic = CASE WHEN :#{#user.pic} IS NOT NULL THEN :#{#user.pic} ELSE u.pic END " +
            "WHERE u.userId = :#{#user.userId}")
    int updateUserInfo(@Param("user") User user);


    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.userId = :userId")
    int deleteUser(@Param("userId") Long userId);

}
