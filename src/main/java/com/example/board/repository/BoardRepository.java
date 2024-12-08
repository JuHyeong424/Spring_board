package com.example.board.repository;

import com.example.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/*
JpaRepository<처리할 entity 클래스, entity의 기본키 타입>
다양한 메서드 바로 사용 가능
save, findById, findAll, deleteById
 */
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
