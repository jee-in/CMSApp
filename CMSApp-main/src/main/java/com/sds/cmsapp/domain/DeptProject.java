package com.sds.cmsapp.domain;

import lombok.Data;

@Data
public class DeptProject {
	private int deptProjectIdx;
	private Dept dept;
	private Project project;
}
