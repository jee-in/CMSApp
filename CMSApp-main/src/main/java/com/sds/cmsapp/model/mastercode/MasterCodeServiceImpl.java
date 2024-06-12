package com.sds.cmsapp.model.mastercode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sds.cmsapp.domain.MasterCode;

@Service
public class MasterCodeServiceImpl implements MasterCodeService {

	@Autowired
	MasterCodeDAO masterCodeDAO;

	@Override
	public MasterCode select(int status_code) {
		return masterCodeDAO.select(status_code); //returnType="MasterCode"
	}

	@Override
	public List<MasterCode> selectAll() {
		return masterCodeDAO.selectAll();
	}
		
}
