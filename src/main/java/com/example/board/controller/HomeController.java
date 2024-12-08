package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
@Controller
클라이언트에서 HTTP 요청을 받아 처리하고 데이터를 모델에 담아 뷰로 전달
뷰 템플릿(JSP, Thymeleaf)와 사용해 뷰 리졸버와 연동
특정 URL 요청 처리할 메서드 정의
 */
@Controller
public class HomeController {

    @GetMapping("/") // 기본 홈 주소 요청
    public String index() {
        return "index"; // index.html
    }
}
