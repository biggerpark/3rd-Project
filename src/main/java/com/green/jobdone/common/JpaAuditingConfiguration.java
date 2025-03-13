package com.green.jobdone.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
// 이거 실행하는 위치(application 에 존재시 모든 테스트때 항상 jpa관련 빈을 tdd mybatis slice test때 문제가 발생해서 분리

public class JpaAuditingConfiguration {

}
