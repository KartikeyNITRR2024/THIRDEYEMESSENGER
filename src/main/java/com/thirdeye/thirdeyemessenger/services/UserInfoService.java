package com.thirdeye.thirdeyemessenger.services;

import java.util.List;
import java.util.Map;

import com.thirdeye.thirdeyemessenger.entity.UserInfo;

public interface UserInfoService {
	public void getUserList() throws Exception;
	public UserInfo getIdToUser(Long userId);
	Map<Long, UserInfo> getAllUsers();
}
