package com.example.demo.controller;

import com.example.demo.controller.exception.BusinessLogicException;
import com.example.demo.controller.exception.ExceptionCode;
import com.example.demo.dto.Amount;
import com.example.demo.dto.KakaoApproveResponse;
import com.example.demo.dto.KakaoCancelResponse;
import com.example.demo.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    // 결제요청
    @PostMapping("/ready")
    public ResponseEntity readyToKakaoPay(Amount amount) {
        return kakaoPayService.kakaoPayReady(amount);
    }

    // 결제 성공
    @GetMapping("/success")
    public ResponseEntity afterPayRequest(@RequestParam("pg_token") String pgToken) {

        log.info("pgToken: {}", pgToken);

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);

        log.info("kakaoApprove: {}", kakaoApprove);

        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }

    //결제 진행 중 취소
    @GetMapping("/cancel")
    public void cancel() {

        throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
    }

    // 결제 실패
    @GetMapping("/fail")
    public void fail() {

        throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
    }

    // 환불
    @PostMapping("/refund")
    public ResponseEntity refund() {

        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel();

        return new ResponseEntity<>(kakaoCancelResponse, HttpStatus.OK);
    }
}