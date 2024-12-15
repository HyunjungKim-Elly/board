package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageRequestDTO {
    private int page; // 현재 페이지 번호
    private int size; // 한 페이지당 항목 수
    private String type; // 검색 조건
    private String keyword; // 검색 키워드

    public PageRequestDTO() {
        this.page = 1;   // 기본값
        this.size = 10; // 기본값
        this.type = null;
        this.keyword = null;
    }
}
