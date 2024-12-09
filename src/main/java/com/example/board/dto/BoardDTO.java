package com.example.board.dto;

import com.example.board.entity.BaseEntity;
import com.example.board.entity.BoardEntity;
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

    // paging 만들 때 사용할 생성자 alt + insert
    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime=boardCreatedTime;
    }

    // entity에서 dto로 전환
    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        return  boardDTO;
    }
}
