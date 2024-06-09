package com.sds.cmsapp.dashboard.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sds.cmsapp.domain.DashboardDocument;
import com.sds.cmsapp.domain.Document;
import com.sds.cmsapp.model.document.DocumentService;
import com.sds.cmsapp.model.project.ProjectService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController(value = "dashboardRestDocumentListcontroller")
public class RestDocumentListController {

	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private ProjectService projectService;

	/*-------------------------------------
	 * [요약] 탭 - 결재 상태별 문서 수 집계
	 -------------------------------------*/
	@GetMapping("/dashboard/list/count")
	public Map getDocumentCountByStatus() {
		Map map = new HashMap();
		
		map.put(200, documentService.countForDashboard(200)); // 200에 리뷰 대기 문서 수 담기
		map.put(300, documentService.countForDashboard(300)); // 300에 리뷰 완료 문서 수 담기
		map.put(500, documentService.countForDashboard(500)); // 500에 반려 문서 수 담기
		
		return map; // Spring boot가 Map을 JSON으로 자동 변환
	}

	/*-------------------------------------
	 *  [요약], [전체보기] 탭 - 목록 불러오기
	-------------------------------------*/
	@GetMapping("/dashboard/list")
	public List<DashboardDocument> getDocumentListByFilter(
			@RequestParam(value = "status_code", required = false) String status_code
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate
			, @RequestParam(value = "projectIds", required = false) List<Integer> projectIds
	// , @RequestParam(value="emp_idx", required=false) int emp_idx,
	// , @RequestParam(value="dept_idx", required=false) int dept_idx
	) {
		
		// 모델에 보낼 map 만들기
		Map map = new HashMap();

		// 조건: 상태 코드
		map.put("status_code", status_code);
		
		// 조건: 날짜
		if(StringUtils.hasText(startDate)) {
			// 날짜 파라미터가 있을 경우 (전체 보기 탭) - String을 Timestamp로 변환
			Timestamp start_date = Timestamp.valueOf(startDate);
			Timestamp end_date = Timestamp.valueOf(endDate);
			map.put("start_date", start_date);
			map.put("end_date", end_date);
		} else {
			// 날짜 파라미터가 없을 경우 (요약 탭)
			map.put("start_date", null);
			map.put("end_date", null); 
		}
		
		// 조건: 프로젝트
		// 현재는 아무 프로젝트도 선택하지 않은 경우 전체 보기의 결과가 나옴...
		if (projectIds != null && projectIds.size() > 0 && projectIds.get(0) != 900) {
			List projects = new ArrayList();
			for (int p : projectIds) {
				System.out.println(p);
				projects.add(p);
			}
			map.put("projects", projects);				
		}
		
		// map.put("emp_idx", emp_idx);
		// map.put("dept_idx", dept_idx);

		// 모델 일 시키기
		List<Document> documentList = documentService.selectAllForDashboard(map);
		log.debug("조회된 문서 수: " + documentList.size());

		// 모델에게 받은 정보를 DashboardDocument DTO에 담기
		List<DashboardDocument> dashboardDocumentList = new ArrayList();

		for (Document d : documentList) {
			// Timestamp를 String으로 변환
			Date date = new Date();
			date.setTime(d.getLatestRegisteredStatusLog().getRegdate().getTime());
			String regdate = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
			// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			log.debug("문서 번호: " + d.getDocumentIdx());
			log.debug("상태 변경일: " + regdate);
			log.debug("현 상태: " + d.getLatestRegisteredStatusLog().getMasterCode().getStatusName());
			
			DashboardDocument dd = new DashboardDocument();
			dd.setRegdate(regdate); // 문서 상태 변경일자
			dd.setDocumentIdx(d.getDocumentIdx()); // 문서 번호
			dd.setVersion(d.getDocumentVersion().getVersionLog().getVersion()); // 버전
			dd.setTitle(d.getDocumentVersion().getVersionLog().getTitle()); // 제목
			dd.setComments(d.getLatestRegisteredStatusLog().getComments()); // 상태 변경 코멘트
			dd.setEmpName(d.getLatestRegisteredStatusLog().getEmp().getEmpName()); // 사원 이름
			dd.setDeptName(d.getLatestRegisteredStatusLog().getEmp().getDept().getDeptName()); // 부서 이름
			dd.setRoleName(d.getLatestRegisteredStatusLog().getEmp().getRole().getRoleName()); // 역할 이름
			dd.setStatusName(d.getLatestRegisteredStatusLog().getMasterCode().getStatusName()); // 상태 이름
			dd.setStatusCode(d.getLatestRegisteredStatusLog().getMasterCode().getStatusCode()); // 상태 이름
			dd.setProjectName(d.getFolder().getProject().getProjectName()); // 프로젝트 이름

			dashboardDocumentList.add(dd);
		}
		return dashboardDocumentList; // Spring boot가 List를 JSON으로 자동 변환
	}
	
	// 프로젝트 필터를 위해 전체 프로젝트 목록을 불러오기 (admin만 해당)
	@GetMapping("/admin/project/list")
	public List getProjectList() {
		return projectService.selectAll();
	}

}
