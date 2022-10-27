package com.example.interview.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.interview.service.UserService;
import com.example.interview.service.UserServiceImpl;

@Controller
public class UserController {

	private final UserService userService;

	public UserController(final UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = "/user", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public ResponseEntity<?> createUser(@RequestBody final Map<String, Object> body) {

		int result = userService.create(body);

		if (result == UserServiceImpl.FAIL_BY_NOT_ALLOWED_NICKNAME) {
			final String recommendedNickname = createRecommendedNickname();
			body.put("nickname", recommendedNickname);

			result = userService.create(body);
		}

		if (result == UserServiceImpl.FAIL_BY_ETC) {
			return ResponseEntity.internalServerError()
				.body("etc");
		}

		if (result == UserServiceImpl.FAIL_BY_DB) {
			return ResponseEntity.internalServerError()
				.body("db");
		}

		if (result == UserServiceImpl.SUCCESS) {
			return handleSuccess(body);
		}

		return handleSuccess(body);
	}

	private String createRecommendedNickname() {
		return UUID.randomUUID().toString();
	}

	private ResponseEntity<?> handleSuccess(final Map<String, Object> body) {
		final String id = (String)body.get("id");
		return ResponseEntity.ok(id);
	}

	@RequestMapping(path = "/users", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> findUsersByName(@RequestParam final String name) {
		final List<Map<String, Object>> users = userService.findByName(name);
		if (users == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(users);
	}
}
