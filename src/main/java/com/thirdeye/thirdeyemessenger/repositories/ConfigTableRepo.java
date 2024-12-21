package com.thirdeye.thirdeyemessenger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdeye.thirdeyemessenger.entity.ConfigTable;

@Repository
public interface ConfigTableRepo extends JpaRepository<ConfigTable, Long> {
}
