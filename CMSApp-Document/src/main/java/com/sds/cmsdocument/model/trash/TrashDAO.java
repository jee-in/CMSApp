package com.sds.cmsdocument.model.trash;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sds.cmsdocument.domain.Trash;

@Mapper
public interface TrashDAO {
	// 휴지통에 넣기
	public int insert(final Trash trash);
	
	// 휴지통에서 제거 (복원, 영구삭제)
	public int delete(final Integer trashIdx);
	
	// 휴지통 비우기
	public int deleteAll();
	
	// 휴지통 한건 조회
	public Trash select(final Integer trashIdx);
	
	// 휴지통 문서 수 조회
	public int selectCount();
	
	// 휴지통 내 문서 전체조회 
	public List<Trash> selectAll();
	
	// 휴지통 내 문서 리스트 조회
	public List<Trash> selectAllWithRange(final Map<String, Integer> map);
	
	// 문서번호로 휴지통 한건 조회
	public Trash selectByDocumentIdx(final int documentIdx);
	
	// 문서번호로 휴지통 Type 한 건 조회
	@Select("SELECT * from trash WHERE document_idx = #{documentIdx}")
	public Trash selectTypeByDocumentIdx(final int documentIdx);
	
	// 휴지통 내의 모든 문서의 idx 조회
	public List<Integer> selectDocumentIdx();
	
	public int deleteByDocumentIdx(final int documentIdx);

}
