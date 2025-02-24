package com.green.jobdone.business;

import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {


    Optional<Business> findByUserIdAndBusinessId(Long businessId, User user);
}
