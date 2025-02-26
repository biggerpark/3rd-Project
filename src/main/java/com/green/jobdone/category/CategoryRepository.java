package com.green.jobdone.category;

import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
