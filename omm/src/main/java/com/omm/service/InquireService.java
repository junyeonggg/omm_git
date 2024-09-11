package com.omm.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omm.dao.InquireDao;
import com.omm.dto.InquireDto;
import com.omm.dto.PagingSearch;

@Service
public class InquireService {
	@Autowired
	private InquireDao inquireDao;
	public List<InquireDto> selectAll(PagingSearch paging) {
		return inquireDao.selectAll(paging);
	}
	public HashMap<String, Object> selectInquireById(int inquire_id) {
		return inquireDao.selectInquireById(inquire_id);
	}
	public void insertInquire(InquireDto inquireDto) {
		inquireDao.insertInquire(inquireDto);
	}
	public int getTotCnt() {
		return inquireDao.getTotCnt();
	}
	public void updateForm(InquireDto inquireDto) {
		inquireDao.updateForm(inquireDto);
	}
	public void deleteInquire(int inquire_id) {
		inquireDao.deleteInquire(inquire_id);
	}

}
