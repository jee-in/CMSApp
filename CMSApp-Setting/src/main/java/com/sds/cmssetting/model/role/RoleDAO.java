package com.sds.cmssetting.model.role;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sds.cmssetting.domain.Role;

@Mapper
public interface RoleDAO {

	public void insert(Role role);
	public void insertRole(Role role);
	public int getMaxRoleCode();
	public Role selectByRoleIdx(int roleIdx);
	public Role selectByRoleCode(int roleCode);
	public List selectAll();
	
}
