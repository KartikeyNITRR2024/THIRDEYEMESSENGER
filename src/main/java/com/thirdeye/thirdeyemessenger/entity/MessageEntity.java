package com.thirdeye.thirdeyemessenger.entity;

import java.sql.Time;
import java.sql.Timestamp;

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
@Table(name = "Messageentity")
@NoArgsConstructor
@Getter
@Setter
public class MessageEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(name = "userid", nullable = false)
    private Integer userId;
    
    @Column(name = "typeofmessage", nullable = false)
    private Integer typeOfMessage;
    
    @Column(name = "timeofgenerating", nullable = false)
    private Timestamp timeofgenerating;
    
    @Column(name = "messagetext", nullable = false)
    private String messageText;
     
}
