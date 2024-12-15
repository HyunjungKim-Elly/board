package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.MemberRepository;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Long register(BoardDTO dto) {
        log.info("Registering new Board: {}", dto);
        
        // Member 객체 조회
        Member member = memberRepository.findById(dto.getWriterEmail())
                .orElseThrow(() -> new IllegalArgumentException("멤버에 없는 이메일입니다: " + dto.getWriterEmail()));

        // DTO -> Entity 변환
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member) // Member 설정
                .build();

        // Board board = dtoToEntity(dto);
        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Board> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize());

        Page<Board> result = repository.getBoardWithWriter(pageable);

        Function<Board, BoardDTO> fn = (board) -> entityToDTO(
                board,
                board.getWriter(),
                0L // 댓글 수 임시 설정
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO read(Long bno) {
        Board board = repository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Board Not Found with BNO: " + bno));

        Member member = board.getWriter();

        return entityToDTO(board, member, 0L);  
    }

    @Override
    public void modify(BoardDTO dto) {
        Board board = repository.findById(dto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("Board Not Found: " + dto.getBno()));

        board.changeTitle(dto.getTitle());
        board.changeContent(dto.getContent());

        repository.save(board);  
    }

    @Override
    public void remove(Long bno) {
        Board board = repository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Board Not Found: " + bno));
        
        repository.delete(board);  
        log.info("삭제되었습니다:", bno);
    }

}
