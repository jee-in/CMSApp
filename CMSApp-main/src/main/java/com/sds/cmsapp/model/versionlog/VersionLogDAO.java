package com.sds.cmsapp.model.versionlog;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sds.cmsapp.domain.VersionLog;

@Mapper
public interface VersionLogDAO {
	
//	// 하나 조회 by version_log_idx
//	public VersionLog selectByVersionLogIdx(int version_log_idx);
//	
//	// 0603 추가
//	public VersionLog select(int version_log_idx);
	
	public VersionLog selectByDocumentIdx(int document_idx);

}
