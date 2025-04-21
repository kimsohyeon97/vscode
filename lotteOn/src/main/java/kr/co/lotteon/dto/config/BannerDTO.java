package kr.co.lotteon.dto.config;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO {

    private int bno;

    private String size;   //배너크기
    //배너종류(메인상단(MAIN1),메인슬라이더(MAIN2), 상품상세보기(PRODUCT1), 회원로그인(MEMBER1), 마이페이지(MY1))
    private String cate;
    private String color;  //배경색
    private String link;   //배너링크
    private String oName;  //기존이미지이름
    private String sName;  //변경이미지이름
    private LocalTime startTime; //노출시간 시작
    private LocalTime endTime;   //노출시간 끝
    private LocalDate startDay;  //노출날짜 시작
    private LocalDate endDay;    //노출날짜 끝
    private String state; // 활성/비활성
}
