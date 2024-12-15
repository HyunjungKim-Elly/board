package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 페이징 처리와 함께 Board와 Member를 조회
    @Query(value = "SELECT b FROM Board b LEFT JOIN FETCH b.writer",
           countQuery = "SELECT COUNT(b) FROM Board b")
    Page<Board> getBoardWithWriter(Pageable pageable);

    @Query("SELECT b, w FROM Board b LEFT JOIN b.writer w WHERE b.bno = :bno")
	Object getBoardWithWriter(@Param("bno") Long bno);
	
	@EntityGraph(attributePaths = {"writer"}) // writer 필드를 즉시 로딩
    Page<Board> findAll(Pageable pageable);
}