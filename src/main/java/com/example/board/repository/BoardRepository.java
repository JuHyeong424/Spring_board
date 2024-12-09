package com.example.board.repository;

import com.example.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
JpaRepository<처리할 entity 클래스, entity의 기본키 타입>
다양한 메서드 바로 사용 가능
save, findById, findAll, deleteById
 */
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    // update board_table set board_hits=board_hits+1 where id=?
    @Modifying // insert, update, delete 등을 수행하는 query
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id); // @Param("id")는 query의 :id와 일치
}
