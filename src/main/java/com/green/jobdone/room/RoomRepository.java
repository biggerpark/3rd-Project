package com.green.jobdone.room;

import com.green.jobdone.entity.Room;
import com.green.jobdone.room.model.RoomDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r.user.userId, r.business.businessId, r.state from Room r WHERE r.roomId = :roomId")
    RoomDto userIdByRoomId(Long roomId);
}
