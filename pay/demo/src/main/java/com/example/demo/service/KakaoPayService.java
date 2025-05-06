package com.example.demo.service;

import com.example.demo.dto.Amount;
import com.example.demo.dto.KakaoApproveResponse;
import com.example.demo.dto.KakaoCancelResponse;
import com.example.demo.dto.KakaoReadyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    static final String cid = "TC0ONETIME"; // 테스트 가맹점 코드
    @Value("${ADMIN_KEY}")
    private String adminKey;
    private KakaoReadyResponse kakaoReady;


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }


    public ResponseEntity<KakaoReadyResponse> kakaoPayReady(Amount amount) {

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", amount.getCid());  // Amount에서 가져온 가맹점 코드
        parameters.add("partner_order_id", amount.getPartner_order_id());  // Amount에서 가져온 주문 번호
        parameters.add("partner_user_id", amount.getPartner_user_id());  // Amount에서 가져온 회원 ID
        parameters.add("item_name", amount.getItem_name());  // Amount에서 가져온 상품명
        parameters.add("quantity", String.valueOf(amount.getQuantity()));  // Amount에서 가져온 수량
        parameters.add("total_amount", String.valueOf(amount.getTotal()));  // Amount에서 가져온 total (총 결제 금액)
        parameters.add("vat_amount", String.valueOf(amount.getTax()));  // Amount에서 가져온 tax (부가세)
        parameters.add("tax_free_amount", String.valueOf(amount.getTax_free()));  // Amount에서 가져온 tax_free (비과세 금액)
        parameters.add("approval_url", "http://localhost:8082/payment/success");
        parameters.add("cancel_url", "http://localhost:8082/payment/cancel");
        parameters.add("fail_url", "http://localhost:8082/payment/fail");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        // 카카오페이에 요청 보내기
        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class
        );

        kakaoReady.setPartner_order_id(amount.getPartner_order_id());
        kakaoReady.setPartner_user_id(amount.getPartner_user_id());

        log.info("kakaoReady: {}", kakaoReady);

        return ResponseEntity.ok(kakaoReady);
    }



    // 결제 완료 승인
    public KakaoApproveResponse approveResponse(String pgToken) {

        log.info("pgToken: {}", pgToken);

        log.info("kakaoReady: {}", kakaoReady);

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", kakaoReady.getPartner_order_id());
        parameters.add("partner_user_id", kakaoReady.getPartner_user_id());
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        return approveResponse;
    }



    // 결제 환불
    public KakaoCancelResponse kakaoCancel() {

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", "환불할 결제 고유 번호");
        parameters.add("cancel_amount", "환불 금액");
        parameters.add("cancel_tax_free_amount", "환불 비과세 금액");
        parameters.add("cancel_vat_amount", "환불 부가세");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);

        return cancelResponse;
    }



}
