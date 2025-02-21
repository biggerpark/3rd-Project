package com.green.jobdone.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.jobdone.service.model.Dto.PostOptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
//이거 어디감
public class ServicePostReq {
    @Schema(title = "서비스 받을 유저 pk", example = "2")
    @JsonIgnore
    private long userId;
    @Schema(title = "받고싶은 서비스 pk", example = "1")
    @NotNull(message = "필수로 입력해야 됩니다.")
    private long productId;
    @Min(value = 0, message = "최소금액은 0원 이상입니다.")
    private int totalPrice;
    private double lat;
    private double lng;
    @NotNull(message = "주소는 필수로 입력해야 됩니다.")
    private String address;
    private String comment;
    @JsonIgnore
    private String phone;
    @Schema(title = "년/월/일 작성", description = "2025/01/22", example = "2025/01/22")
    @NotNull(message = "필수로 입력해야 됩니다.")
    private String startDate;
    @Schema(title = "시작 시간", description = "11:00:00", example = "11:00:00")
    @NotNull(message = "필수로 입력해야 됩니다.")
    private String mStartTime;
    @Schema(title = "이름", example = "필수작성")
    @JsonIgnore
    private String name;
    @Schema(title = "평수", description = "0")
    @Min(value = 0, message = "평수는 0이상이여야 합니다.")
    private Integer pyeong;

    private List<PostOptionDto> options;
    @JsonIgnore
    private long serviceId;
}
