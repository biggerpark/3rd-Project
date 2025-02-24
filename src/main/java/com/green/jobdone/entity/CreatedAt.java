package com.green.jobdone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass //Entity 부모역할
@EntityListeners(AuditingEntityListener.class) //이벤트 연결(binding), insert가 될 때 현재일시값을 넣자.
public class CreatedAt {
    @CreatedDate //insert가 되었을 때 현재일시값을 넣는다. 이 애노테이션이 작동을 하려면 @EntityListeners 세팅이 되어 있어야 한다.
    @Column(nullable = false, columnDefinition = "DATETIME(0)") //이 애노테이션은 자동으로 작성이 되는데 설정을 좀 더 해주고 싶다면 이 애노테이션을 붙여야 한다.
    @ColumnDefault("current_timestamp()")
    private LocalDateTime createdAt;
//    @PrePersist
//    public void setCreatedAt() {
//        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
//    }
}
