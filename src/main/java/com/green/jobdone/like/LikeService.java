package com.green.jobdone.like;

import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.Like;
import com.green.jobdone.entity.LikeIds;
import com.green.jobdone.entity.User;
import com.green.jobdone.like.model.LikeGetRes;
import com.green.jobdone.like.model.LikePostReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeMapper mapper;
    private final AuthenticationFacade authenticationFacade;
    private final LikeRepository likeRepository;

    @Transactional
    public int postLikeInfo(LikePostReq p) {

        long userId=authenticationFacade.getSignedUserId();

        p.setUserId(userId);

        // User ENTITY 설정
        User user=new User();
        user.setUserId(userId);

        //Business ENTITY 설정
        Business business=Business.builder()
                .businessId(p.getBusinessId())
                .build();

        // 복합키 LikeIds ENTITY 설정
        LikeIds likeIds=new LikeIds();
        likeIds.setUserId(userId);
        likeIds.setBusinessId(p.getBusinessId());

        //Like ENTITY 설정
        Like like=new Like();
        like.setLikeIds(likeIds);
        like.setBusiness(business);
        like.setUser(user);
//        int result = mapper.deleteLikeInfo(p); 커밋테스트


        // 특정 유저의 특정 비즈니스 좋아요 삭제
        int result = likeRepository.deleteByUserIdAndBusinessId(userId, p.getBusinessId());

        if (result == 1) {
            return 0;
        }

        likeRepository.save(like);


//        int result1 = mapper.postLikeInfo(p);

        return 1;

    }


    public List<LikeGetRes> getLikeInfo() {

        long userId=authenticationFacade.getSignedUserId();

        List<LikeGetRes> res = mapper.getLikeInfo(userId);

        for (LikeGetRes r : res) {
            r.setPic(PicUrlMaker.makePicUrlBusiness(r.getBusinessId(), r.getPic()));

            // 업체사진 끌어올거면 이거 써야함.

        }

        return res;

    }
}
