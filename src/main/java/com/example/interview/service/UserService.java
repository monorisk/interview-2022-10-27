package com.example.interview.service;

import java.util.List;
import java.util.Map;

import org.springframework.lang.Nullable;

public interface UserService {

	int create(final Map<String, Object> body);

	@Nullable
	List<Map<String, Object>> findByName(final String name);
}
