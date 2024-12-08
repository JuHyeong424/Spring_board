package com.example.board.dto;

import lombok.*;

import java.time.LocalDateTime;

// DTO(Data Transfer Object) 데이터 전송할 때 사용하는 객체. VO, Bean / Entity
@Getter // lombok을 이용해 메서드 자동 생성
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits; // 조회수
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;
}
