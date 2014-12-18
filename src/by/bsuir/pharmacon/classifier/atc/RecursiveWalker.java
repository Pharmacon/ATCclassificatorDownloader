package by.bsuir.pharmacon.classifier.atc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RecursiveWalker implements Constants {
	/**
	 * Web driver entity
	 */
	WebDriverEntity driver;
	/**
	 * is current element code equals true, else equals false
	 */
	boolean isCode;
	/**
	 * String to put into file
	 */
	String temp;
	/**
	 * ATC category name
	 */	
	String catName;	
	/**
	 * Current directory path
	 */
	static String curDirPath;
	/**
	 * Current category name
	 */
	String curCategoryName;
	/**
	 * list of all parent elements of ATC code
	 */	
	ArrayList<String> spider = new ArrayList<>() ;
	/**
	 * constructor
	 */
	public RecursiveWalker() {			
	 driver = new WebDriverEntity();
	}
	/**
	 *Performs recursive walk of ATC tree
	 *top level categories
	 */
	public void walkATCClassification(){
		List <WebElement> topelems = driver.getDriver().findElements(By.xpath(XPATH_FOR_TOP_GROUP));
		List<String> topNames = createNamesListFromWebelemList(topelems);
		for(int i=0;i<topNames.size();i++){
			createCategoryFolder(topNames.get(i));
			WebElement currentElementATC = driver.getDriver().findElement(By.linkText(topNames.get(i)));
			spider.add(topNames.get(i).split(" ")[0].trim());
			currentElementATC.click();
			walkSecondLevelCategory(topNames.get(i));
		    driver.getDriver().navigate().back();
		    spider.remove(spider.size()-1);
		}
}
	/**
	 *Performs recursive walk of second level category
	 */
	private void walkSecondLevelCategory(String topName) {
		List<WebElement> thelem = driver.getDriver().findElements(By.xpath(SECOND_LEVEL_PATH));
		List<String> topcatNums = createListOfCodesFromWeblems(thelem);
		createATCscsFile(topName, topcatNums, FIRST);
	    walkSubcat();
	}
	/**
	 *Performs recursive walk of categories after 2level
	 */
	public void walkSubcat() {
	    List <WebElement> secondLavelElems = driver.getDriver().findElements(By.xpath(SECOND_LEVEL_PATH));
	    List<String> names = createNamesListFromWebelemList(secondLavelElems);
	      	for (String name :names) {
					System.out.println("   "+name);
					driver.getPause().until(ExpectedConditions.elementToBeClickable(By.xpath(SECOND_LEVEL_PATH.concat("[contains(text(),'"+name.split(" ")[0]+"')]"))));
					WebElement thelem = driver.getDriver().findElement(By.xpath(SECOND_LEVEL_PATH.concat("[contains(text(),'"+name.split(" ")[0]+"')]")));
					String curCatNum = thelem.getText().trim().split(" ")[0];
					curCategoryName = thelem.getText().replace(curCatNum, "").trim();
					//String fullCatName= thelem.getText();
					
					thelem.click();
					spider.add(curCatNum);
					checkIfElementIsCode();	
					List <WebElement> templevelem = driver.getDriver().findElements(By.xpath(SECOND_LEVEL_PATH));
					if(!templevelem.isEmpty()){
					    List<String> templevelemnames = createNamesListFromWebelemList(templevelem);
					    if(!driver.getDriver().findElements(By.xpath(IMPACT_SUBSTANCE_VALUE_PATH)).isEmpty())
					    {
					    	createATCscsFile(name, templevelemnames, SECOND);
					    }
					    else if(!isCode){		
					    	createATCscsFile(name, templevelemnames, FIRST);
					    }
					    else{
					    	createATCscsFile(name, templevelemnames, THIRD);
					    }
					}
					walkSubcat();					
					driver.getDriver().navigate().back();	
					spider.remove(spider.size()-1);
	    	}
	}
	/**
	 * check if element is ATC code
	 */
	private void checkIfElementIsCode() {
		List<WebElement> webelemtocheck = driver.getDriver().findElements(By.xpath(SECOND_LEVEL_PATH));
		if(webelemtocheck.size()>0)
		{ 	
			webelemtocheck.get(0).click();
			isCode = driver.getDriver().findElements(By.xpath(SECOND_LEVEL_PATH)).size()>0||!driver.getDriver().findElement(By.xpath(CODE_PATH)).getText().split(" ")[0].matches("[a-zA-Z]{1}\\d{2}[a-zA-Z]{2}\\d{2}")?false:true;
			if(isCode)
			{
				String codeName = driver.getDriver().findElement(By.xpath(CODE_PATH)).getText();
				createATCscsFile(codeName, null, CODE);
			}
			driver.getDriver().navigate().back();
		}
	}
	/**
	 * Create number list instance
	 * @param list
	 * @return
	 */
	public String createNumListInstance(List<String> list){
		String result="";
	    for (String elem :list) {
	    	result += elem.split(" ")[0].trim()+";";
	    }
	    //remove last symbol
		return result.substring(0,result.length()-1);
	}
	/**
	 * Create instance that contains elements that was checked from  route to code element
	 * @param list
	 * @param atc
	 * @return
	 */
	public String createSpiderInstance(List<String> list,  String atc){
		String result= atc + "\n<- concept_ATC_code;;\n";
	    for (String elem :list) {
	    	result += spiderPattern.replace("ELEM", elem).replace("CODE_ATC", atc).concat("\n");
	    }
		return result;
	}
	/**
	 * Create list of names from list of webelements
	 * @param topelems
	 * @return
	 */
	private List<String> createNamesListFromWebelemList( List<WebElement> topelems) {
		List<String> topNames = new  ArrayList<>();
    		for (WebElement topElemName :topelems) {
    			topNames.add(topElemName.getText());
    		}
		return topNames;
	}
	/**
	 * Create list of codes in format "A,B,C"
	 * @param thelem
	 * @return
	 */
	private List<String> createListOfCodesFromWeblems(List<WebElement> thelem) {
		List<String> topcatNums = new  ArrayList<>();
		for (WebElement elem2 :thelem) {
			topcatNums.add(elem2.getText().split(" ")[0].trim());
			}
		return topcatNums;
	}
	/**
	 * Create folder or category name
	 * @param categoryName
	 */
	private void createCategoryFolder(String categoryName) {
		curDirPath = rootFolderPath+categoryName;
		new File(curDirPath).mkdir();
	}
	/**
	 * create ATC scs file 
	 * @param topName - name of top category
	 * @param topcatNums - code of category
	 * @param levelTemplate - name of template
	 */
	private void createATCscsFile(String topName, List<String> topcatNums, String levelTemplate) {
		switch(levelTemplate){
		case FIRST:
			temp = firstLevelTemplate.replace("GROUP_ATC", topName.split(" ")[0].trim()).replace("DESCRIPTION", topName.substring(topName.split(" ")[0].trim().length(),topName.length()).trim()).replace("SUBDIV_LIST", createNumListInstance(topcatNums));
			break;
		case SECOND:
			temp = groupAndCodeTemplate.replace("GROUP_ATC", topName.split(" ")[0].trim()).replace("DESCRIPTION", curCategoryName).replace("SUBDIV_LIST", createNumListInstance(topcatNums));	
			break;
		case THIRD:
			temp = secondLevelTemplate.replace("GROUP_ATC", topName.split(" ")[0].trim()).replace("DESCRIPTION", curCategoryName);
			break;
		case CODE:
			String codeName = driver.getDriver().findElement(By.xpath(CODE_PATH)).getText();
			temp = codeTemplate.replace("CODE_ATC", codeName.split(" ")[0]).replace("DESCRIPTION", codeName.replace("(¿“’)", "").replace(codeName.split(" ")[0], "").trim());
			temp += createSpiderInstance(spider, codeName.split(" ")[0]);
		}
		if (!temp.equals(null))
			{
			 try {
				FileUtils.writeStringToFile(new File(curDirPath+"\\"+topName.split(" ")[0].trim()+".scs"), temp,"UTF8");
				} catch (IOException e) {
				e.printStackTrace();
				}	
			}
	}
}
