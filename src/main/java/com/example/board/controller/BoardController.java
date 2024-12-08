package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String save(@ModelAttribute BoardDTO boardDTO) {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }
}
