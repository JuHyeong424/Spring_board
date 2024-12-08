package com.example.board.entity;

import com.example.board.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// DB 테이블 역할을 하는 클래스
// service와 repository에서만 사용
@Entity
@Getter
@Setter
@Table(name = "board_table") // db 테이블 이름 명시적 지정
public class BoardEntity extends BaseEntity {

    @Id // 기본키 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment(mysql 기준)
    private Long id;

    @Column(length = 20, nullable = false) // column 설정(크기 20, null 불가)
    private String boardWriter;

    @Column // default(크기 255, null 가능)
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    // dto에 담긴 값을 entity로 전환
    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        return boardEntity;
    }
}
