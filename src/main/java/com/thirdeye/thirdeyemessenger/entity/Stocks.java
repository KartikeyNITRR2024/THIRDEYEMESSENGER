package com.thirdeye.thirdeyemessenger.entity;

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
@Table(name = "STOCKS")
@NoArgsConstructor
@Getter
@Setter
public class Stocks {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(name = "stockname", nullable = false)
    private String stockName;
    
    @Column(name = "stocksymbol", nullable = false, unique = true)
    private String stockSymbol;
     
    @Column(name = "marketname", nullable = false)
    private String marketName;
}
