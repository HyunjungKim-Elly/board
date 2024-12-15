package org.zerock.board.service;


import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

public interface BoardService {

    Long register(BoardDTO dto);
    BoardDTO read(Long bno);
    void modify(BoardDTO boardDTO);
    void remove(Long bno);

    default Board dtoToEntity(BoardDTO dto){

        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }

    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writerEmail(member != null ? member.getEmail() : "No Email") // Null 체크
                .writerName(member != null ? member.getName() : "No Name") // Null 체크
                .replyCount(replyCount != null ? replyCount.intValue() : 0)
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        return boardDTO;

    }
    
    PageResultDTO<BoardDTO, Board> getList(PageRequestDTO pageRequestDTO);
}
