package com.green.jobdone.youtube.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YoutubeDto {
    private long videoId;
    private String title;
    private String keyword;

}
