package com.green.jobdone.business;

import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.BusinessPic;
import com.green.jobdone.entity.BusinessPicIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessPicRepository extends JpaRepository<BusinessPic, Long> {

  
}
