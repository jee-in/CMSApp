package com.sds.cmsapp.model.document;

import java.util.List;
import java.util.Map;

import com.sds.cmsapp.domain.Document;
import com.sds.cmsapp.domain.VersionLog;

public interface DocumentService {

	// 전체 문서 수 조회
	public int selectCount();

	// 모든 문서 조회
	public List selectAll(Map map);

	// 선택 문서 조회
<<<<<<< HEAD
	public Document selectByDocumentIdx(int document_idx);
}
=======
	public Document select(int document_idx);
	
	//글 작성 폼
	public void insert(VersionLog versionLog);
}
>>>>>>> d5b96bd4a6b5c749a560c335aff1aad77a1210a1
