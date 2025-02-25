package com.green.jobdone.room;

import com.green.jobdone.business.BusinessRepository;
import com.green.jobdone.common.PicUrlMaker;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.entity.Business;
import com.green.jobdone.entity.Room;
import com.green.jobdone.entity.User;
import com.green.jobdone.room.chat.model.ChatPostReq;
import com.green.jobdone.room.model.RoomGetReq;
import com.green.jobdone.room.model.RoomGetRes;
import com.green.jobdone.room.model.RoomPostReq;
import com.green.jobdone.user.UserRepository;
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
    private final RoomRepository roomRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
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

        Room room = new Room();
        com.green.jobdone.entity.Service service = new com.green.jobdone.entity.Service();
        if(p.getServiceId()!=null && p.getServiceId()>0){
            room.setService(service);
        }

        User user = userRepository.findById(p.getUserId()).orElse(null);
        Business business = businessRepository.findById(p.getBusinessId()).orElse(null);
        room.setBusiness(business);
        room.setUser(user);
        roomRepository.save(room);

//        int res = roomMapper.insRoom(p);
//        return p.getRoomId();
        return room.getRoomId();
    }
}
