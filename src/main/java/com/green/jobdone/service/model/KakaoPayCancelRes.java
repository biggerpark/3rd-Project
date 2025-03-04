package com.green.jobdone.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.green.jobdone.service.model.Dto.Amount;
import com.green.jobdone.service.model.Dto.ApprovedCancelAmount;
import com.green.jobdone.service.model.Dto.CancelAmount;
import com.green.jobdone.service.model.Dto.CancelAvailableAmount;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class KakaoPayCancelRes {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    @JsonProperty("partner_order_id")
    private String partnerOrderId;
    @JsonProperty("partner_user_id")
    private String partnerUserId;
    @JsonProperty("payment_method_type")
    private String paymentMethodType;
    private Amount amount;
    @JsonProperty("approved_cancel_amount")
    private CancelAmount approvedCancelAmount;
    @JsonProperty("canceled_amount")
    private CancelAmount canceledAmount;
    @JsonProperty("cancel_available_amount")
    private CancelAmount cancelAvailableAmount; // 3개가 동일한데 같은 타입으로 써도 되는거네..
    @JsonProperty("item_name")
    private String itemName;
    @JsonProperty("item_code")
    private String itemCode;
    private Integer quantity;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("approved_at")
    private LocalDateTime approvedAt;
    @JsonProperty("canceled_at")
    private LocalDateTime canceledAt;
    private String payload;
}
