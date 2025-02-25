package com.green.jobdone.ocrwebclient.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class OCRWebClientDto {
    private String businessName;
    private String address;
    private String busiCreatedAt;
    private String businessNum;
}
