package com.green.jobdone.youtube;

import com.green.jobdone.config.YoutubeConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
@RequiredArgsConstructor
@Service
public class YoutubeService {


    private final RestTemplate restTemplate;
    private final YoutubeConfig youtubeConfig;

    public String getYoutubeVideo(String youtubeUrl) {
        String videoId = extractVideoId(youtubeUrl);

        String apiUrl = String.format("https://www.youtube.com/watch?v=%s&key=%s&part=snippet"
                , videoId, youtubeConfig.getApiKey());
        return restTemplate.getForObject(apiUrl, String.class);

    }


    //유튜브 비디오 추출
    public String extractVideoId(String youtubeUrl) {

        if (youtubeUrl != null && youtubeUrl.contains("v=")) {
            String[] split = youtubeUrl.split("v="); // v=를 기준으로 문자열을 나눈다
            String videoId = split[1];//아이디 부분 추출 ㅇㅇ
            int ampersandIndex = videoId.indexOf('&'); //만약 &있으면 그것도 잘라내야함
            if (ampersandIndex != -1) {
                videoId = videoId.substring(0, ampersandIndex);
            }
            return videoId;
        }
        return null;
    }
}
