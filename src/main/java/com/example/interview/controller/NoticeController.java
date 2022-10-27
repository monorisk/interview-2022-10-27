package com.example.interview.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.interview.dao.NoticeDAO;

@Controller
public class NoticeController {
	private static final String ERR_NOTICE_NOT_EXIST = "notice not exist";

	private final NoticeDAO noticeDAO;

	public NoticeController(final NoticeDAO noticeDAO) {
		this.noticeDAO = noticeDAO;
	}

	/*
	# 면접을 위한 코드 의도 설명 #

	noticeId로 notice를 조회하는 코드
	현재는 notice를 DB에서 조회하여 바로 반환하는 행위 밖에 없음
	 */
	@RequestMapping(path = "/notice/{noticeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Map<String, Object> findNotice(@PathVariable final long noticeId) {
		final Map<String, Object> notice = noticeDAO.find(noticeId);
		if (notice.isEmpty()) {
			throw new RuntimeException(ERR_NOTICE_NOT_EXIST);
		}

		return notice;
	}

	/*
	# 면접을 위한 코드 의도 설명 #

	noticeId에 해당하는 notice가 없으면 404를 반환함

	면접의 원활한 진행을 위해 나머지 예외는 모두 500으로 응답하기로 가정함
	 */
	@ExceptionHandler
	public ResponseEntity<Map<String, Object>> handleException(final Exception cause) {
		if (ERR_NOTICE_NOT_EXIST.equals(cause.getMessage())) {
			return ResponseEntity.notFound()
				.build();
		} else {
			return ResponseEntity.internalServerError()
				.build();
		}
	}
}
