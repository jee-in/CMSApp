package com.sds.cmssetting.model.versionlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sds.cmssetting.domain.VersionLog;

@Service
public class VersionLogServiceImpl implements VersionLogService {
	
	@Autowired
	private VersionLogDAO versionLogDAO;

//	// 하나 조회 by version_log_idx
//	@Override
//	public VersionLog selectByVersionLogIdx(int version_log_idx) {
//		return versionLogDAO.selectByVersionLogIdx(version_log_idx);
//	}
//	
//	// 0603 추가
//	public VersionLog select(int version_log_idx) {
//		return versionLogDAO.select(version_log_idx);
//	};
	
	public VersionLog selectByDocumentIdx(int document_idx) {
		return versionLogDAO.selectByDocumentIdx(document_idx);
	};

}
