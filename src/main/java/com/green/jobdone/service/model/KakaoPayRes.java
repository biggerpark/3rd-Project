package com.green.jobdone.service.model;

import com.green.jobdone.service.model.Dto.Amount;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayRes {
    private String aid; // 요청 고유 번호
    private String tid; // 결제 고유 번호
    private String cid; // 가맹점 코드
    private String sid; // 정기 결제용 id
    private String partner_order_id; // 가맹점 주문번호(serviceId)
    private String partner_user_id; // 가맹점 회원 id(userId pk)
    private String payment_method_type; //결제 수단(card 또는 money)
    private Amount amount; //결제 금액 정보
    private String item_name; // 상품이름
    private String item_code; //상품 코드
    private int quantity; // 상품 수
    private String created_at; // 결제 준비 요청 시각
    private String approved_at; // 결제 승인 시각
    private String payload; // 결제 승인 요청에 대한 저장한 값? 요청시 전달된 내용
}
