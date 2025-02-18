package com.green.jobdone.business.model.get;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessGetMonthlyReq {
    @JsonIgnore
    private long signedUserId;

    @Schema(title = "businessId")
    private long businessId;

    public BusinessGetMonthlyReq(long businessId) {
        this.businessId = businessId;
    }
}
