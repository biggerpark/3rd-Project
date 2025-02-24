package com.green.jobdone.room;

import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.room.chat.model.ChatPostReq;
import com.green.jobdone.room.model.RoomGetReq;
import com.green.jobdone.room.model.RoomGetRes;
import com.green.jobdone.room.model.RoomPostReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final RoomMapper roomMapper;
    private final AuthenticationFacade authenticationFacade;
    public List<RoomGetRes> selRoom(RoomGetReq p) {
        if(p.getBusinessId()==null){
            p.setUserId(authenticationFacade.getSignedUserId());
        }
        List<RoomGetRes> res = roomMapper.selRoom(p);
        for(RoomGetRes r : res){

            r.setPic(PicUrlMaker.makePicUserUrl(r.getUserId(),r.getPic()));
            r.setLogo(PicUrlMaker.makePicUrlLogo(r.getBusinessId(),r.getLogo()));
        }
        return res;
    }

    public Long insRoom(RoomPostReq p) {
        p.setUserId(authenticationFacade.getSignedUserId());
        if(p.getServiceId()==0){
            p.setServiceId(null);
        }
        int res = roomMapper.insRoom(p);

        return p.getRoomId();
    }
}
