package com.green.jobdone.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayCancelReq {
    private String cid;
    private String tid;
    @JsonProperty("cancel_amount")
    private int cancelAmount;
    @JsonProperty("cancel_tax_free_amount")
    private Integer cancelTaxFreeAmount;
    @JsonProperty("cancel_vat_amount")
    private Integer cancelVatAmount;
}
