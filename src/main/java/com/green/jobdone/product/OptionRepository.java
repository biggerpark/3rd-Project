package com.green.jobdone.product;

import com.green.jobdone.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option,Long> {

    @Query("select o.name from Option o " +
            "join o.product p where p.business.businessId =:businessId")
    List<String> findOptionNameByBusinessId(Long businessId);
}
