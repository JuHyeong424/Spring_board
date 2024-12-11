package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor // fianl와 @NonNull 필드를 포함하는 생성자를 자동 생성
@RequestMapping("/board") // '/board'로 시작하는 요청 시
public class BoardController {

    /*
    의존성 주입
    객체 간의 의존 관계를 객체 내부에서 직접 생성하지 않고, 외부에서 주입하여
    객체의 결합도를 낮추고 코드의 재사용성과 테스트 용이성을 높임
     */
    private final BoardService boardService;

    @GetMapping("/save") // '/board/save'로 들어오는 get 요청 시
    public String saveForm() {
        return "save"; // save.html
    }

    @PostMapping("/save") // /board/save로 들어오는 post 요청 처리
    // BoardDTO 클래스를 찾아서 save.html에 있는 name과 필드 값이 같으면 필드에 setter로 값 담음
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/")
    // 데이터를 db에서 가져올때 model 이용
    public String findAll(Model model) {
        // db에서 전체 게시글 데이터를 가져와서 list.html에 보여줌
        // 목록이 여러 개이므로 dto가 담긴 list 이용
        List<BoardDTO> boardDTOList = boardService.findAll();
        // dto를 model에 boardList로 저장
        model.addAttribute("boardList", boardDTOList);
        return "list"; // list.html
    }

    @GetMapping("/{id}") // board/{id} get 요청 처리
    public String findById(@PathVariable Long id, Model model, @PageableDefault(page=1) Pageable pageable) {
        // @PathVariable로 경로상 값 가져와서 model로 값 담아서 이동
        /*
        해당 게시글의 조회수를 하나 올리고
        게시글 데이터를 가져와서 detail.html에 출력
         */

        boardService.updateHits(id); // 해당 id에 헤당하는 게시글 조회수 1 증가
        BoardDTO boardDTO = boardService.findById(id); // 해당 id 게시글의 id를 dto에 저장
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    // 요청 파라미터가 BoardDTO 객체의 필드에 자동 바인딩
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "detail";
        // return "redirect:/board/" + boardDTO.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
        /*
        redirect를 쓰지 않으면 클라이언트는 여전히 /delete/{id} URL에 머물러서
        브라우저를 새로고침하면 /delete/{id}로 요청이 다시 전송되오 중복 삭제가 발생
        redirect로 서버가 요청을 처리한 후 클라이언트가 새로운 요청을 보내도록 유도.
        POST 요청으로 서버의 상태를 변경한 후, 새로운 GET 요청으로 리다이렉트하여 같은 작업 반복 막음
         */
    }

    // /board/paging?page=1
    @GetMapping("/paging") // /board/paging 요청 처리
    // @PageableDefault는 Pageable 객체 기본값 1로
    // Pageable는 페이징 정보 담는 객체
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        // 현재 요청된 페이지 번호 반환
        // pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3; // 보여지는 페이지 수
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        // page 갯수 20개
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9
        // 보여지는 페이지 갯수 3개

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";
    }
}
