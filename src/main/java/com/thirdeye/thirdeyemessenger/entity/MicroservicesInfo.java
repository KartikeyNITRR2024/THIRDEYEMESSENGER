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
import lombok.ToString;

@Entity
@Table(name = "Microservicesinfo")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MicroservicesInfo {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(name = "microservicename", nullable = false)
    private String microserviceName;
    
    @Column(name = "microserviceurl", nullable = false)
    private String microserviceUrl;
    
    @Column(name = "microserviceuniqueid", nullable = false)
    private Integer microserviceUniqueId;
}
