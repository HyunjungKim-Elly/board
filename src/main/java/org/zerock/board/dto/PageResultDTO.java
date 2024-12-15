package org.zerock.board.dto;

import lombok.Getter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

@Getter
public class PageResultDTO<DTO, EN> {
    private List<DTO> dtoList; // DTO 리스트
    private int totalPage;     // 전체 페이지 수
    private int page;          // 현재 페이지 번호
    private int size;          // 페이지당 항목 수
    private int start, end;    // 페이지네이션 시작/끝 번호
    private boolean prev, next; // 이전/다음 페이지 여부
    private List<Integer> pageList; // 페이지 번호 리스트

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        // Page<Entity> -> DTO 리스트 변환
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());

        this.totalPage = result.getTotalPages();
        this.page = result.getNumber() + 1; // 현재 페이지 번호 (0부터 시작이므로 +1)
        this.size = result.getSize();

        // 페이지네이션 범위 설정
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;
        start = tempEnd - 9;
        end = Math.min(totalPage, tempEnd);

        prev = start > 1;
        next = totalPage > end;

        // 페이지 번호 리스트 생성
        pageList = java.util.stream.IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }
}
