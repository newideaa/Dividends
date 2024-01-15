package com.example.dividends.entity;

import com.example.dividends.dto.Company;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Entity(name="COMPANY")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;
    private String name;

    public CompanyEntity(Company company){
        this.ticker = company.getTicker();
        this.name = company.getName();
    }
}
