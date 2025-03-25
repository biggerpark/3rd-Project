package com.green.jobdone.category;

import com.green.jobdone.entity.DetailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailTypeRepository extends JpaRepository<DetailType, Long> {
    @Query("SELECT d FROM DetailType d WHERE d.category.categoryId = :categoryId")
    List<DetailType> findByCategoryId(@Param("categoryId") Long categoryId);
}
