package com.thirdeye.thirdeyemessenger.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdeye.thirdeyemessenger.entity.MessageEntity;

@Repository
public interface MessageEntityRepo extends JpaRepository<MessageEntity, Long> {
	List<MessageEntity> findByUserIdAndTypeOfMessageOrderByTimeofgenerating(Integer userId, Integer typeOfMessage);
}

