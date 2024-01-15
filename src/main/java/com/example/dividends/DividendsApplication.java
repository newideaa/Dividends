package com.example.dividends;

import com.example.dividends.dto.Company;
import com.example.dividends.scraper.Scraper;
import com.example.dividends.scraper.YahooFinanceScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividendsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DividendsApplication.class, args);

        System.out.println("Main -> " + Thread.currentThread().getName());
    }
}
