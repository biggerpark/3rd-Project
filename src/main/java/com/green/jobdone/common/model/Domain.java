package com.green.jobdone.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Domain {
    private String server;

    public Domain(@Value("${domain.path.server}") String server) {
        this.server = server;
    }
}
