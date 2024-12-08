package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.BaseEntity;
import com.example.board.entity.BoardEntity;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
service에서는 주로
DTO -> Entity(controller에서 받아와 repository에 넘길때. Entity class)
Entity -> DTO(조회할 때. repository에서 entity 받아 controller로 dto 넘김. DTO class)
 */

@Service
@RequiredArgsConstructor // fianl 필드 생성자 자동 생성
public class BoardService {

    private final BoardRepository boardRepository;

    public void save(BoardDTO boardDTO) {

        // boardRepository.save()를 통해 db에 값 저장
        // boardRepository는 entity를 받으므로 dto를 entity로 반환
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }
}
