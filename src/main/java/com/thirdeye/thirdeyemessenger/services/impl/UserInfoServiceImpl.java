package com.thirdeye.thirdeyemessenger.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye.thirdeyemessenger.entity.UserInfo;
import com.thirdeye.thirdeyemessenger.repositories.UserInfoRepo;
import com.thirdeye.thirdeyemessenger.services.UserInfoService;


@Service
public class UserInfoServiceImpl implements UserInfoService {

    private Map<Long, UserInfo> idToUser = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Override
	public void getUserList() throws Exception {
    	Map<Long, UserInfo> idToUser1 = new HashMap<>();
        logger.info("Starting to fetch userls list.");
        try {
            List<UserInfo> users = userInfoRepo.findAll();
            for(UserInfo user : users)
            {
            	idToUser1.put(user.getId(), user);
            }
            lock.writeLock().lock();
            idToUser.clear();
            idToUser = new HashMap<>(idToUser1);
            logger.info("Successfully fetched all {} users.", idToUser1.size());
        } catch (Exception e) {
            logger.error("Error occurred while fetching users list : {}", e.getMessage(), e);
            throw new Exception("Failed to retrieve user list.", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
	public UserInfo getIdToUser(Long userId) {
    	UserInfo user = null;
    	lock.readLock().lock();
        try {
	    	if(idToUser.containsKey(userId))
	    	{
	    		user = idToUser.get(userId);
	    	}
	        return user;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
	public Map<Long, UserInfo> getAllUsers() {
    	lock.readLock().lock();
        try {
	        return idToUser;
        } finally {
            lock.readLock().unlock();
        }
    }
}