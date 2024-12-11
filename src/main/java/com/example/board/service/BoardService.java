package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.BaseEntity;
import com.example.board.entity.BoardEntity;
import com.example.board.entity.BoardFileEntity;
import com.example.board.repository.BoardFileRepository;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
service에서는 주로
DTO -> Entity(controller에서 받아와 repository에 넘길때. Entity class)
Entity -> DTO(조회할 때. repository에서 entity 받아 controller로 dto 넘김. DTO class)
 */

@Service
@RequiredArgsConstructor // fianl 필드 생성자 자동 생성
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDTO boardDTO) throws IOException {

        // 파일 첨부 여부에 따라 로직 분리
        if (boardDTO.getBoardFile().isEmpty()) {
            // 첨부 파일 없음
            // boardRepository.save()를 통해 db에 값 저장
            // boardRepository는 entity를 받으므로 dto를 entity로 반환
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        } else {
            // 첨부 파일 있음
            /*
            1. DTO에 담긴 파일을 꺼냄
            2. 파일의 이름 가져옴
            3. 서버 저장용 이름을 생성
            // 내사진.jpg -> 1323123_내사진.jpg
            4. 저장 경로 설정
            5. 해당 경로에 파일 저장
            6. board_table에 해당 데이터 save 처리
            7. board_file_table에 해당 데이터 save 처리
             */
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO); // dto를 entity로
            Long savedId = boardRepository.save(boardEntity).getId(); // 6 부모 게시글의 pk인 id 필요해서 getId()
            BoardEntity board = boardRepository.findById(savedId).get(); // 부모 entity를 db에서 가져옴
            for (MultipartFile boardFile: boardDTO.getBoardFile()) {
               // MultipartFile boardFile = boardDTO.getBoardFile(); // 1
                String originalFilename = boardFile.getOriginalFilename(); // 2
                String storedFileNames = System.currentTimeMillis() + "_" + originalFilename; // 3
                String savePath = "C:/Users/asdf/study-spring/springboot_img/" + storedFileNames; // 4
                boardFile.transferTo(new File(savePath)); // 5


                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileNames); // boardfileentity 객체로 전환
                boardFileRepository.save(boardFileEntity); // 7
            }
        }
    }

    @Transactional // toBoardDTO에서 boardEntity가 boardFileEntity에 접근함
    public List<BoardDTO> findAll() {
        // repository에서 찾으므로 entity로 list
        // entity를 dto로 변환 후 controller로 옮겨줌
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            // boardEntityList에서 하나씩 boardEntity에 담아 dto로 변환후 저장
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional // Jpa 관리 메서드가 아니라 추가 메서드(updateHits)를 사용할 때
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional // toBoardDTO에서 boardEntity가 boardFileEntity에 접근함
    public BoardDTO findById(Long id) {
        // repository 메서드로 id 찾아서 entity에 담기
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get(); // optional 벗기고
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity); // dto로 전환
            return boardDTO;
        } else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        // dto를 entity로
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        // 해당 entity가 있으면 update, entity가 없으면 save
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId()); // id로 entity를 dto로
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() -1; // 요청된 페이지 번호. JPA는 페이지 번호 0부터 시작하고 사용자는 보통 1부터 요청하므로 1 뺌
        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수
        // 한 페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작. 실제 요청 페이지에서 1 뺀 값
        // PageRequest.of(): 페이징과 정렬 정보를 포함하는 객체. page는 0 기반 페이지 번호, pageLimit는 한 페이지에서 표시할 데이터 개수, id를 기준으로 내림차순 정렬
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // Page 타입 살리면서 entity(=board) 객체를 dto로 바꿈(map()). 원하는 정보만 dto 생성자를 통해 얻음
        // 목록: id, writer, title, hits, createdTitle
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}
