package com.thirdeye.thirdeyemessenger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thirdeye.thirdeyemessenger.entity.UserInfo;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, Long> {
}
