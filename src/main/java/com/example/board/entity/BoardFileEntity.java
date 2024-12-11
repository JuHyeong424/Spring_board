package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    // 게시판은 여러 파일 포함할 수 있지만, 각 파일은 하나의 게시판에만 속함. 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY) // 데이터 로딩 전략. 지연 로딩(LAZY. 필요할때 가져옴)으로 BoardFileEntity는 실제로 접근할 때 db에서 조회됨
    @JoinColumn(name = "board_id") // 현재 entity의 table에서 외래키로 사용될 컬럼 이름 board_id. BoardEntity의 기본 키를 참조하는 외래키
    private BoardEntity boardEntity; // 하나의 게시판(BoardEntity)는 여러 개의 파일(BoardFileEntity)을 포함할 수 있지만, 각 파일은 하나의 게시판에만 속함

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }
}
