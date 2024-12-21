package com.thirdeye.thirdeyemessenger.entity;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "UserInfo")
@NoArgsConstructor
@Getter
@Setter
public class UserInfo {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String Name;
    
    @Column(name = "mobileNo")
    private String mobileNo;
     
    @Column(name = "email")
    private String email;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "telegram_groupid_1")
    private String telegramGroupId1;
    
    @Column(name = "telegram_groupid_2")
    private String telegramGroupId2;
    
    @Column(name = "telegram_groupid_3")
    private String telegramGroupId3;
    
    @Column(name = "is_active")
    private Integer isActive;
}
