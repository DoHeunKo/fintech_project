package com.ms.fintech.crawling;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import com.ms.fintech.dtos.CrawlerDto;

@Component
public class Crawler {
	private WebDriver driver;
	public List<CrawlerDto> process() throws InterruptedException {
		var list = new ArrayList<CrawlerDto>();
		System.setProperty("webdriver.edge.driver", "src/main/resources/msedgedriver/msedgedriver.exe");
		var opt = new EdgeOptions();
//		opt.addArguments("hand-less");
		opt.setHeadless(true);
		opt.addArguments("--remote-allow-origins=*");
        driver = new EdgeDriver(opt);
        //브라우저 선택
        driver.get("https://www.careet.net/Content/Series/2");
        WebElement ele = driver.findElement(By.cssSelector(".posi-r > .sublist-area"));
        new Actions(driver)
        .scrollByAmount(0, 50000)
        .scrollByAmount(0, 50000)
        .scrollByAmount(0, 50000)
        .scrollByAmount(0, 50000)
        .scrollByAmount(0, 50000)
        .scrollByAmount(0, 50000)
        .scrollByAmount(0, 50000)
        .perform();
        var str = ele.getText();
//        var strr = str.subSequence(7, size).toString();
        var strr2 = str.split("\\R");
//        System.out.println(strr);
        Thread.sleep(1000);
        int k = 0;
        for (int i = 2; i < 9; ++i) {
        	for (int j = 1; j < 7; ++j) {
        		var img = driver.findElement(By.cssSelector("#contentArea > div:nth-child("+i+") > ul > li:nth-child("+j+") > a > div.list-thumb > img"));
        		var src = driver.findElement(By.cssSelector("#contentArea > div:nth-child("+i+") > ul > li:nth-child("+j+") > a"));
        		
        		list.add(new CrawlerDto(strr2[k++], img.getAttribute("src"), src.getAttribute("href")));
//        		System.out.println(img.getAttribute("src"));
        	}
        }
        return list;
//        System.out.println(list);
    }
}