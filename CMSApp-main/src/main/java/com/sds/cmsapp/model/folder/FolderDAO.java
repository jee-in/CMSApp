package com.sds.cmsapp.model.folder;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sds.cmsapp.domain.Folder;

@Mapper
public interface FolderDAO {
	
	// 폴더 추가
	public int insert(final Folder folder);
	
	// 폴더 제거
	public int delete(final int folderIdx);
	
	// 폴더 수정
	public int update(final Folder folder);
	
	// 폴더 한건 조회
	public Folder select(final int folderIdx);
	
	// 모든폴더 조회
	public List<Folder> selectAll();
	
	// 하위폴더 조회
	public List<Folder> selectSub(final int folderIdx);
		
	// 상위폴더 조회
	public Folder selectParent(final int folderIdx);
	
	// 최상위 폴더 조회
	public List<Folder> selectTopFolder();
	
	// 프로젝트 idx로 폴더 조회
	public List<Folder> selectByProjectIdx();
	
}
