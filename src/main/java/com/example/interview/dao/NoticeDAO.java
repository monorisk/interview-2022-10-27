package com.example.interview.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

public final class NoticeDAO {

	private static final String NAME_SPACE = "com.example.interview.dao.NoticeDAO.";

	private final SqlSessionTemplate sqlSessionTemplate;

	public NoticeDAO(final SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	/*
	# 면접을 위한 코드 의도 설명 #

	해당 코드는 매우 심플하게 DB에서 데이터 조회만 수행하고 바로 반환
	쿼리는 질문의 핵심적인 요소가 아니라 생략
	 */
	public Map find(final long noticeId) {
		final Map params = new HashMap<>();
		params.put("noticeId", noticeId);

		// sqlSessionTemplate은 쿼리 수행 중 예외 발생 시 org.apache.ibatis.exceptions.IbatisException을 상속한 예외를 발생 시킴
		final Map notice = sqlSessionTemplate.selectOne(NAME_SPACE + "find", params);

		return notice == null ? Collections.emptyMap() : notice;
	}
}
