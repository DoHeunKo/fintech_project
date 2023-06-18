package com.ms.fintech.crawling;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

public class Crawler {
	private WebDriver driver;

	private static final String url = "https://yourei.jp/腕を磨く";
	public void process() {
		System.setProperty("webdriver.edge.driver", "C:\\Users\\hee\\git\\fintech_project\\src\\main\\resources\\msedgedriver");
		//크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)
		
		driver = new EdgeDriver();
		//브라우저 선택
		
		try {
		    getDataList();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		
		driver.close();	//탭 닫기
		driver.quit();	//브라우저 닫기
	}
	 private List<String> getDataList() throws InterruptedException {
	        List<String> list = new ArrayList<>();

	        driver.get(url);    //브라우저에서 url로 이동한다.
	        Thread.sleep(1000); //브라우저 로딩될때까지 잠시 기다린다.

	        //WebElement sentence = driver.findElement(By.cssSelector("#sentence-example-list .sentence-list li"));
	        //System.out.println(sentence.getText());
	        //この先腕を磨いていけば、いつかはこの男に勝てる日がくるのだろうか。 ...
	        //ベニー松山『風よ。龍に届いているか(下)』
	        // findElement (끝에 s없음) 는 해당되는 선택자의 첫번째 요소만 가져온다

	        List<WebElement> elements = driver.findElements(By.cssSelector("#sentence-example-list .sentence-list li"));
	        for (WebElement element : elements) {
	            System.out.println("----------------------------");
	            System.out.println(element);	//⭐
	        }
	        
	        return list;
	    }
}
