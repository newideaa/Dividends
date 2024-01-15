package com.example.dividends.scraper;

import com.example.dividends.dto.Company;
import com.example.dividends.dto.Dividend;
import com.example.dividends.dto.ScrapedResult;
import com.example.dividends.enums.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper{

    private static final String URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400; // 60 * 60 * 24(1일)

    @Override
    public ScrapedResult scrap(Company company) {
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000; // 현재 시간을 밀리 세컨드로 받는 것
            String url = String.format(URL, company.getTicker(), START_TIME, now);

            //1. http connection
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            //2. document로 가져오기
            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableEle = parsingDivs.get(0);

            //3. 다른 사이트에서 스크랩핑할 때, 그 사이트 규칙에 맞게 스크래핑하면 된다.
            Element tbody = tableEle.children().get(1);

            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()) {
                String text = e.text();
                if (!text.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = text.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if(month < 0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }

                dividends.add(new Dividend(LocalDateTime.of(year, month, day, 0, 0), dividend));


            }
            scrapResult.setDividends(dividends);

        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }

        return scrapResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker){
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try{
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(0);
            String title = titleEle.text().split(" - ")[0].trim();
            // def

            return new Company(ticker, title);

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
