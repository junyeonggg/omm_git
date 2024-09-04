package com.omm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omm.dao.InquireDao;
import com.omm.dto.InquireDto;

@Service
public class InquireService {
	@Autowired
	private InquireDao inquireDao;
	public List<InquireDto> selectAll() {
		return inquireDao.selectAll();
	}
	public InquireDto selectInquireById(int id) {
		
		return inquireDao.selectInquireById(id);
	}

}
