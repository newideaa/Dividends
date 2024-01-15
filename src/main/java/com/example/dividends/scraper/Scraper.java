package com.example.dividends.scraper;


import com.example.dividends.dto.Company;
import com.example.dividends.dto.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
