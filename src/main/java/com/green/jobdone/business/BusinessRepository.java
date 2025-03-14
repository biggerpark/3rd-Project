package com.green.jobdone.business;

import com.green.jobdone.business.model.*;
import com.green.jobdone.entity.Business;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("select b.user.userId from Business b where b.businessId=:businessId")
    Long findUserIdByBusinessId(@Param("businessId") Long businessId); //로그인한 유저가 업체 관리자인가?

    @Query("select b.businessId from Business b where b.user.userId=:UserId")
    Long findBusinessIdByUserId(@Param("UserId") Long UserId);


    @Query("SELECT COUNT(*) FROM Business b WHERE b.businessNum =:businessNum")
    Integer findExistBusinessNum(@Param("businessNum") String businessNum); //사업자 등록번호 조회



    @Query("select b.user.userId from Product p join p.business b where p.productId=:productId")
    Long findUserIdByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("update Business b set b.title=:#{#p.title} ,b.contents=:#{#p.contents} where b.businessId=:#{#p.businessId}")
    void updateBusinessContents(@Param("p") BusinessContentsPostReq p); //컨텐츠 및 기타 수정


    @Query("select count(s.serviceId) from Service s " +
            "join s.product p " +
            "join p.business b " +
            "where b.businessId = :businessId")
    Integer countBusinessServices(@Param("businessId") Long businessId);

    @Modifying
    @Query("update Business b set b.state =:#{#p.state} where b.businessId =:#{#p.businessId}")
    Integer updateBusinessState(@Param("p") BusinessStatePutReq p);


    @Modifying
    @Query("update Business set paper=:#{#p.paper} where businessId=:#{#p.businessId}")
    Integer updateBusinessPaper(@Param("p") BusinessPaperPatchReq p);

    @Modifying
    @Query("update Business set logo=:#{#p.logo} where businessId=:#{#p.businessId}")
    Integer updateBusinessLogo(@Param("p") BusinessLogoPatchReq p);

    @Query("select b.logo from Business b where b.businessId =:businessId")
    String findBusinessLogoByBusinessId(@Param("businessId") Long businessId);

    @Modifying
    @Query("update Business  b set b.thumbnail =:#{#p.thumbnail} where b.businessId =:#{#p.businessId}")
    int patchBusinessThumbnail(BusinessPatchThumbnailReq p);
}
