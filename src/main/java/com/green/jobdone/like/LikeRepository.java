package com.green.jobdone.like;

import com.green.jobdone.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Like u WHERE u.user.userId = :userId")
    int deleteUser(@Param("userId") Long userId);
}
