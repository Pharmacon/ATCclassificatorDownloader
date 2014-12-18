package by.bsuir.pharmacon.classifier.atc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverEntity implements Constants{
	/**
	 * Web driver entity
	 */
	WebDriver driver = new FirefoxDriver();
	/**
	 * Web driver pause
	 */
	WebDriverWait pause = new WebDriverWait(driver, 500);
	
	public WebDriverEntity() {
		driver.get(URL);	
		navigateToATCPage();
	}
	/**
	 *performs navigation from main page to ATC page 
	 *using left navigation tab
	 */
	public void navigateToATCPage(){
		WebElement atcButton = driver.findElement(By.xpath(XPATH_FOR_LEFT_ATC_BUTTON));
		atcButton.click();
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	public WebDriverWait getPause() {
		return pause;
	}
	public void setPause(WebDriverWait pause) {
		this.pause = pause;
	}
	
}
