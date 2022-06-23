package com.hansol.schedule.Utils;


import com.hansol.schedule.dto.Paging;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PagingUtils {
	
	public static Paging setPaging(int total, int nowPage, int cntPerPage) {

		int cntPage =5; //보여줄 페이지 수
		//cntPerPage=8; //페이지 당 보여줄 항목 수
		
		int lastPage=(int) Math.ceil((double)total / (double)cntPerPage);
		
		int endPage=((int)Math.ceil((double)nowPage / (double)cntPage)) * cntPage;
		if (lastPage < endPage) {
			endPage=lastPage;
		}
		
		int startPage= (endPage - cntPage + 1) < 1 ? 1 : (endPage - cntPage + 1);
		
		int end = 1;
		int start = 1;
		
		if(total >0) {
			end = nowPage * cntPerPage ;
			start = end - cntPerPage +1;
		}
		Paging paging = Paging.builder()
				.nowPage(nowPage)
				.total(total)
				.startPage(startPage)
				.endPage(endPage)
				.lastPage(lastPage)
				.start(start)
				.end(end).build();		
		return paging;
	}
}
