package com.green.jobdone.category;

import com.green.jobdone.category.detail.model.DetailTypeGetReq;
import com.green.jobdone.category.detail.model.DetailTypeGetRes;
import com.green.jobdone.category.detail.model.DetailTypePostReq;
import com.green.jobdone.category.model.CategoryDelReq;
import com.green.jobdone.category.model.CategoryGetRes;
import com.green.jobdone.category.model.CategoryPostReq;
import com.green.jobdone.common.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
@Tag(name = "카테고리 관리")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "카테고리 등록,관리자가 등록")
    public ResultResponse<Integer> postCategory(@RequestBody CategoryPostReq p) {
        try {
            return ResultResponse.<Integer>builder()
                    .resultMessage("카테고리 등록 완료")
                    .resultData(categoryService.postCategory(p))
                    .build();
        } catch (IllegalArgumentException e) {
            return  ResultResponse.<Integer>builder()
                    .resultMessage(e.getMessage())
                    .build();
        }
    }

    @PostMapping("/detail")
    public ResultResponse<Integer> postDetailType(@RequestBody DetailTypePostReq p){
        try {
            return ResultResponse.<Integer>builder()
                    .resultMessage("상세 서비스 등록 완료")
                    .resultData(categoryService.postDetailType(p))
                    .build();
        }catch (IllegalArgumentException e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage(e.getMessage())
                    .build();
        }
    }

    @GetMapping
    public ResultResponse<List<CategoryGetRes>> getAllCategory() {
        return ResultResponse.<List<CategoryGetRes>>builder().resultMessage("카테고리 조회 완료")
                .resultData(categoryService.getCategory())
                .build();
    }

    @GetMapping("/detail")
    public ResultResponse<List<DetailTypeGetRes>> getAllDetailType(@Valid @ParameterObject @ModelAttribute DetailTypeGetReq p){
        return ResultResponse.<List<DetailTypeGetRes>>builder().resultMessage("상세 서비스 조회 완료")
                .resultData(categoryService.getDetailType(p)).build();
    }

//    @DeleteMapping
//    public ResultResponse<Integer> delCategory(@RequestBody CategoryDelReq p) {
//        categoryService.delCategory(p);
//        return ResultResponse.<Integer>builder()
//                .resultMessage("삭제 완료")
//                .resultData(1)
//                .build();
//    }

    @DeleteMapping
    @Operation(summary = "관리자가 잘못 등록된 카테고리 삭제")
    public ResultResponse<Integer> deleteCategory(@RequestParam long categoryId){
        return ResultResponse.<Integer>builder()
                .resultMessage("카테고리 삭제 완료")
                .resultData(categoryService.deleteCategory(categoryId))
                .build();
    }
}




