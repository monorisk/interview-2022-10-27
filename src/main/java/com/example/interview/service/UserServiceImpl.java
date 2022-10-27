package com.example.interview.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.IbatisException;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import com.example.interview.dao.UserDAO;

public final class UserServiceImpl implements UserService {
	public static final int SUCCESS = 0;
	public static final int FAIL_BY_NOT_ALLOWED_NICKNAME = 1;
	public static final int FAIL_BY_DB = 2;
	public static final int FAIL_BY_ETC = 3;

	private final UserDAO userDAO;
	private final RestTemplate restTemplate;

	public UserServiceImpl(final UserDAO userDAO, final RestTemplate restTemplate) {
		this.userDAO = userDAO;
		this.restTemplate = restTemplate;
	}

	/*
	# 면접을 위한 코드 의도 설명 #

	전달된 인자를 이용하여 유저를 생성 및 영속성 레이어에 저장하고 SMS을 발송
	성공여부나 각 실패 사유를 반환함
	 */
	@Override
	public int create(final Map<String, Object> body) {
		final String id = (String)body.get("id");
		final String name = (String)body.get("name");
		final String nickname = (String)body.get("nickname");
		final String birthday = (String)body.get("birthday");
		final String mobileNo = (String)body.get("mobileNo");
		final String email = (String)body.get("email");

		try {
			userDAO.insertUser(id, name, nickname, birthday, mobileNo, email);
		} catch (final IbatisException e) {
			return FAIL_BY_DB;
		} catch (final RuntimeException e) {
			if (isFailedByNotAllowedNickname(e)) {
				return FAIL_BY_NOT_ALLOWED_NICKNAME;
			} else {
				return FAIL_BY_ETC;
			}
		}

		sendSms(mobileNo);

		return SUCCESS;
	}

	private void sendSms(final String mobileNo) {
		final String sendSmsUrl = "https://naver.com/send/sms/{mobileNo}";

		restTemplate.postForObject(sendSmsUrl, null, String.class, mobileNo);
	}

	private boolean isFailedByNotAllowedNickname(final Exception cause) {
		return "notAllowedNickname".equals(cause.getMessage());
	}

	/*
	# 면접을 위한 코드 의도 설명 #

	영속성 레이어를 이용하여 이름이 같은 사용자를 조회하고 결과를 반환
	 */
	@Override
	@Nullable
	public List<Map<String, Object>> findByName(final String name) {
		return userDAO.findByName(name);
	}
}
