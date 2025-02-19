package com.green.jobdone.room.chat.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter

public class ChatDto {
    private MultipartFile pics;
    private ChatPostReq p;
}
