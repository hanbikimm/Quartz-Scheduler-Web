package com.hansol.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Paging {
	private int nowPage; //현재 페이지
	private int total; // 총 row 수
	private int startPage; // 보여줄 시작 페이지 
	private int endPage; //보여줄 마지막 페이지
	private int lastPage; // 총 페이지 수
	
	//데이터를 가져올 때 필요한 시작 인덱스와 끝 인덱스
	private int start;
	private int end;
}
