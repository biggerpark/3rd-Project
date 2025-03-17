package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "qa_view")
public class QaView {
    @EmbeddedId
    private QaViewsIds qaViewsIds;

    @ManyToOne
    @JoinColumn(name = "qaId")
    @MapsId("qaId")
    private Qa qa;

    @ManyToOne
    @JoinColumn(name = "userId")
    @MapsId("userId")
    private User user;

    @ColumnDefault("1")
    private int viewCount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();


    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void increaseViewCount() {
        this.viewCount++;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canIncreaseViewCount() {
        return updatedAt.isBefore(LocalDateTime.now().minusDays(1));
    }



}
