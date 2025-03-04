package com.green.jobdone.service.model.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelAvailableAmount {
    private Integer total;
    @JsonProperty("tax_free")
    private Integer taxFree;
    private Integer vat;
    private Integer point;
    private Integer discount;
    @JsonProperty("green_deposit")
    private Integer greenDeposit;
}
