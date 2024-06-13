package com.sds.cmsapp.model.project;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sds.cmsapp.domain.Project;

@Mapper
public interface ProjectDAO {
	
	public int insert(Project project);
	public Project selectByProjectIdx(int projectIdx);
	public List selectAll();
	public void delete(Project project);
	
}
