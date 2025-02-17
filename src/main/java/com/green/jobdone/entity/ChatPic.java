package com.green.jobdone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "chat_pic")
public class ChatPic extends CreatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long chatPicId;

    @ManyToOne
    @JoinColumn(name = "chatId")
    private Chat chat;

    @Column(length = 100)
    private String pic;

}
