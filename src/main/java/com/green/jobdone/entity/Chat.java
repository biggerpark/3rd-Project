package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString
public class Chat extends CreatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    @Column(length = 3000)
    private String contents;

    @ColumnDefault("0")
    private int flag;
}
