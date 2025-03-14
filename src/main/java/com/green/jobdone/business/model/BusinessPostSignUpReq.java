package com.green.jobdone.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Schema(title="업체 컨텐츠 등록 요청")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class BusinessPostSignUpReq {

    @JsonIgnore
    private long businessId;
    @JsonIgnore
    private String paper;
    @JsonIgnore
    private String logo;
    @JsonIgnore
    private String thumbnail;
    @JsonIgnore
    private long signedUserId;
    @JsonIgnore
    private String safeTel;

    @Schema(title = "사업자번호", example = "0000000", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 10, max = 10, message = "사업자번호는 10자리여야 합니다.")
    private String businessNum;
    @Schema(title = "업체 이름", example = "싹 박멸해", requiredMode = Schema.RequiredMode.REQUIRED)
    private String businessName;
    @Schema(title = "업체 주소", example = "만경관근처", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;
    @Schema(title = "서비스 유형", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long detailTypeId;
    @Schema(title = "회사설립일", example = "2019/06/08", requiredMode = Schema.RequiredMode.REQUIRED)
    private String busiCreatedAt;
    @Schema(title = "회사전번", example = "0533836669", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tel;
    private BigDecimal lat;
    private BigDecimal lng;

}
