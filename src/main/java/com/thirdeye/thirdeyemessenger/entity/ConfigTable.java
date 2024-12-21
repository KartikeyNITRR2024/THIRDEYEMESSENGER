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
@Table(name = "Configtable")
@NoArgsConstructor
@Getter
@Setter
public class ConfigTable {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(name = "noofmachineforlivemarket", nullable = false)
    private Integer noOfMachineForLiveMarket;
    
    @Column(name = "noofmachineforholdedstokes", nullable = false)
    private Integer noOfMachineForHoldedStokes;
    
    @Column(name = "timegap", nullable = false)
    private Long timeGap;
     
    @Column(name = "starttime", nullable = false)
    private Time startTime;
    
    @Column(name = "endtime", nullable = false)
    private Time endTime;
    
    @Column(name = "resettime", nullable = false)
    private Time resettime;
    
    @Column(name = "noofuser", nullable = false)
    private Integer noofuser;
    
    @Column(name = "telegrambottoken", nullable = false)
    private String telegramBotToken;
}
