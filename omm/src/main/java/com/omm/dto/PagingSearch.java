package com.omm.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PagingSearch {
	private int current_page;
	private int totDataCnt; // 전체 데이터 갯수
	private int recordSize = 40; // 한번에 보여줄 데이터 갯수 | 입력받을 값
	private int totPageCnt; // 전체 페이지 갯수 (전체-1)/recordSize) +1
	private int startRecord; // 현재 페이지 * 한번에 보여줄 데이터 갯수 : 다음 페이지의 시작record 인덱스
	
	private int pageSize = 10; //한번에 보여줄 페이지 갯수 | 입력받을 값
	private int startPage;
	private int endPage;
	private boolean prevPage;
	private boolean nextPage;
	
	// 검색기능
	private String keyword;
	
	// 필터링
	private String method;
	private String status;
	private String ingre;

	
	public PagingSearch(int totDataCnt,int current_page){
		this.totDataCnt = totDataCnt;
		this.totPageCnt = ((totDataCnt-1)/recordSize)+1;
		this.current_page = current_page;
		this.startRecord = (current_page-1)*recordSize;
		this.startPage = ((current_page-1) / pageSize)*pageSize+1;
		this.endPage = startPage+pageSize -1;
		if(endPage > totPageCnt) endPage = totPageCnt;
		this.prevPage = startPage != 1;
		this.nextPage = endPage != totPageCnt;
	}
	
}
