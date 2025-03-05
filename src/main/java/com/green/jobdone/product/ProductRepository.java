package com.green.jobdone.product;


import com.green.jobdone.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p.productId from Product p where p.business.businessId =:businessId")
    Long findProductIdByBusinessId(@Param("businessId") Long businessId);
}
