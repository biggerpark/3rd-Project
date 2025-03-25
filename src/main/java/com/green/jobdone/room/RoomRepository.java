package com.green.jobdone.room;

import com.green.jobdone.entity.Room;
import com.green.jobdone.room.model.RoomDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT new com.green.jobdone.room.model.RoomDto(r.user.userId, r.business.businessId, r.state) from Room r WHERE r.roomId = :roomId")
    RoomDto userIdByRoomId(Long roomId);

    @Modifying
    @Transactional
    @Query("update Room r set r.state =:state where r.roomId=:roomId")
    void updateStateByRoomId(@Param("state") String state,@Param("roomId") Long roomId);
}
