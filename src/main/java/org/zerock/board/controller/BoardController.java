package org.zerock.board.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/board/")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("PageRequestDTO: {}", pageRequestDTO);

        // PageResultDTO 반환
        PageResultDTO<BoardDTO, Board> result = boardService.getList(pageRequestDTO);

        // 모델에 추가
        model.addAttribute("result", result);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "board/list";
    }


    @GetMapping("/register")
    public void register() {
        log.info("regiser get...");
    }

    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes) {

           try {
            Long bno = boardService.register(dto);
            redirectAttributes.addFlashAttribute("msg", "게시글이 성공적으로 등록되었습니다. (BNO: " + bno + ")");
            redirectAttributes.addFlashAttribute("isError", false); // 성공
        } catch (IllegalArgumentException e) {
            log.error("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("msg", "에러: " + e.getMessage());
            redirectAttributes.addFlashAttribute("isError", true); // 에러
        }


        return "redirect:/board/list";
    }
    @GetMapping("/read")
    public String read(@RequestParam("bno") Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("Board Read Page: {}", bno);

        // BoardDTO 가져오기
        BoardDTO boardDTO = boardService.read(bno);

        if (boardDTO == null) {
            // 게시글이 존재하지 않는 경우 처리
            log.error("Board Not Found: {}", bno);
            return "redirect:/board/list?error=notfound";
        }

        model.addAttribute("dto", boardDTO);
        model.addAttribute("requestDTO", pageRequestDTO);

        return "board/read";
    }
    
    @GetMapping("/modify")
    public String modify(@RequestParam("bno") Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("Board Modify Page: {}", bno);

        // 게시글 데이터 가져오기
        BoardDTO boardDTO = boardService.read(bno);

        if (boardDTO == null) {
            // 게시글이 존재하지 않을 때
            log.error("Board Not Found for Modify: {}", bno);
            return "redirect:/board/list?error=notfound";
        }

        model.addAttribute("dto", boardDTO);
        model.addAttribute("requestDTO", pageRequestDTO);

        return "board/modify";
    }
    
    @PostMapping("/modify")
    public String modifyPost(BoardDTO dto, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
        log.info("Modifying Board: {}", dto);

        boardService.modify(dto);

        redirectAttributes.addAttribute("bno", dto.getBno());
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());

    return "redirect:/board/read"; // 수정 후 read 페이지로 리다이렉트
    }

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {
        log.info("Remove POST request for bno: {}", bno);

        // 삭제 서비스 호출
        boardService.remove(bno);

        // 삭제 완료 메시지 추가
        redirectAttributes.addFlashAttribute("msg", "게시글이 성공적으로 삭제되었습니다");

        return "redirect:/board/list"; // 삭제 후 리스트 페이지로 리다이렉트
    }

}
