package com.example.interview.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.lang.Nullable;

public final class UserDAO {

	private static final String NAME_SPACE = "com.example.interview.dao.UserDAO.";

	private final SqlSessionTemplate sqlSessionTemplate;

	public UserDAO(final SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	/*
	# 면접을 위한 코드 의도 설명 #

	User를 DB에 저장하는 기능
	 */
	public void insertUser(final String id, final String name, final String nickname,
		final String birthday, final String mobileNo, final String email) {

		if (hasNotAllowedCharacter(nickname)) {
			throw new RuntimeException("notAllowedNickname");
		}

		final Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("name", name);
		params.put("nickname", nickname);
		params.put("birthday", birthday);
		params.put("mobileNo", mobileNo);
		params.put("email", email);

		// sqlSessionTemplate은 쿼리 수행 중 예외 발생 시 org.apache.ibatis.exceptions.IbatisException을 상속한 예외를 발생 시킴
		sqlSessionTemplate.insert(NAME_SPACE + "insertUser", params);
	}

	private boolean hasNotAllowedCharacter(final String nickname) {
		return true; // 비허용 문자열 검사는 핵심이 아니라 생략
	}

	/*
	# 면접을 위한 코드 의도 설명 #

	모든 유저들 중 동일한 name을 가지는 유저들 조회
	원활한 면접 진행을 위해 성능 이슈는 없다고 가정
	 */
	@Nullable
	public List<Map<String, Object>> findByName(final String name) {
		final Map<String, Object> params = new HashMap<>();
		params.put("name", name);

		/*
			# 면접을 위한 코드 의도 설명 #

			원활한 면접을 위해,
			동일한 name을 가진 User가 없을 경우 sqlSessionTemplate.selectList는 null을 리턴한다고 가정

			sqlSessionTemplate은 쿼리 수행 중 예외 발생 시 org.apache.ibatis.exceptions.IbatisException을 상속한 예외를 발생 시킴
		 */
		return sqlSessionTemplate.selectList(NAME_SPACE + "findByName", params);
	}
}
