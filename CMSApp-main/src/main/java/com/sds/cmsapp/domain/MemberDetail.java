package com.sds.cmsapp.domain;

import lombok.Data;

@Data
public class MemberDetail {
	private int memberDetailIdx;
	private Member member;
	private String memberPw;
	private String memberPhone;
	private String memberAddr;
}
