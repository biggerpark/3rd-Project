package com.green.jobdone.category;

import com.green.jobdone.category.detail.model.DetailTypeGetReq;
import com.green.jobdone.category.detail.model.DetailTypeGetRes;
import com.green.jobdone.category.detail.model.DetailTypePostReq;
import com.green.jobdone.category.model.CategoryGetRes;
import com.green.jobdone.category.model.CategoryPostReq;
import com.green.jobdone.entity.Category;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    //카테고리

    //JPA 완료
    @Transactional
    public int postCategory(CategoryPostReq p) {

        int exists = categoryMapper.existCategory(p.getCategoryName());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        Category category = Category.builder()
                .categoryName(p.getCategoryName())
                .build();


        categoryRepository.save(category);

        return 1;
    }

    public List<CategoryGetRes> getCategory() {

        return categoryMapper.getCategory();
    }

    //여기부터 디테일

    public int postDetailType(DetailTypePostReq p) {

        int exists = categoryMapper.existDetailType(p.getCategoryId(),p.getName());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 상세서비스입니다");
        }
        return categoryMapper.insDetailType(p);

    }

    public List<DetailTypeGetRes> getDetailType(DetailTypeGetReq p) {
        return categoryMapper.getDetailType(p);
    }



    @Transactional
    public int deleteCategory(long categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
            return 1;  // 성공 시 1 반환
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 수 없는 카테고리입니다.");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다.");
        }
    }


}
