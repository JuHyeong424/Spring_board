package com.example.board.entity;

import com.example.board.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @Column
    private int fileAttached; // 1 or 0으로 파일 첨부 여부 확인

    /*
    BoardEntity가 여러 개의 BoardFileEntity를 가질 수 있는 일대다
    mappedBy = "boardEntity": 외래키는 BoardFileEntity에 정의된 boardEntity 필드에 의해 관리됨
    cascade = CascadeType.REMOVE: BoardEntity가 삭제될 때 관련된 BoardFileEntity도 함께 삭제
    orphanRemoval = true: boardFileEntityList에서 삭제된 엔티티는 자동으로 db에서도 삭제됨
    fetch = FetchType.LAZY: BoardFileEntity 리스트는 실제로 접근할 때만 로드됨
    BoardEntity가 가진 모든 BoardFileEntity를 저장
     */
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    // dto에 담긴 값을 entity로 전환
    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 첨부 파일 없음
        return boardEntity;
    }

    // update 갱신
    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        return boardEntity;
    }

    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(1); // 첨부 파일 있음
        return boardEntity;
    }
}
