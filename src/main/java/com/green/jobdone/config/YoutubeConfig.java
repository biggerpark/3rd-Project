package com.green.jobdone.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

//이걸 활용하여 포폴에 유튜브영상을 표시하는 기능을 구현
@Getter
@Configuration
public class YoutubeConfig {

    @Value("${youtube.api.key}")
    private String apiKey;


}
