package com.green.jobdone.admin;

import com.green.jobdone.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    @Query("SELECT COUNT(a) > 0 FROM Admin a WHERE a.aId = :aId")
    boolean existsByAId(@Param("aId")String aId);

    @Query("select a.aPw from Admin a where a.aId =:aId")
    String findAPwdByAId(@Param("aId")String aId);

    Admin findByaId(String aId);
}
