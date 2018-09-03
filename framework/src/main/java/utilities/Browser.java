package utilities;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Exceptions.TitleMismatchException;
import Exceptions.URLMismatchException;
import Exceptions.TabsCountMismatchException;

public class Browser {
	
	private WebDriver driver;
	private WebDriverWait wait;
	private WebElement ele;
	private List<WebElement> eleList;
	private Select dplist;
	
	private static int datanum=4;
	
	public Browser() {
		String exepath = "C:\\Users\\M1046846\\Downloads\\chromedriver\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exepath);
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 5);
	}
	
	public void verifyTabsCount(ArrayList<String> data) throws TabsCountMismatchException
	{
		int i=driver.getWindowHandles().size();
		if( ( Integer.parseInt(data.get(3)) ) == i )
		{
			return;
		}
		throw new TabsCountMismatchException();
	}
	public void stopTill(ArrayList<String> data) throws NumberFormatException, InterruptedException
	{
		Thread.sleep(Integer.parseInt(data.get(3)));
	}
	
	public void reload(ArrayList<String> data)
	{
		driver.navigate().refresh();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verifyTitle(ArrayList<String> data) throws TitleMismatchException
	{
		String s=driver.getTitle();
		if(s.equals(data.get(3)))
		{
			return;
		}
		throw new TitleMismatchException();
	}
	public void verifyUrl(ArrayList<String> data) throws URLMismatchException
	{
		String s=driver.getCurrentUrl();
		if(s.equals(data.get(3)))
		{
			return;
		}
		throw new URLMismatchException();
	}
	
	public WebDriver getDriver()
	{
		return driver;
	}
	public void openBrowser(ArrayList<String> data)
	{
		driver.get(data.get(3));
		driver.manage().window().maximize();
	}
	public void closeBrowser(ArrayList<String> data)
	{
		driver.quit();
	}
	public WebElement find(String path)
	{
		ele=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(path)));
		return ele;
	}
	public List<WebElement> findall(String path)
	{
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(path)));
		eleList=driver.findElements(By.xpath(path));
		return eleList;
	}
	public void clickOn(ArrayList<String> data)
	{
		ele=this.find(data.get(datanum));
		ele.click();
	}
	public void sendData(ArrayList<String> data)
	{
		ele=this.find(data.get(datanum));
		ele.sendKeys(data.get(datanum+1));
	}
	public void selectElement(ArrayList<String> data)
	{
		ele=this.find(data.get(datanum));
		dplist= new Select(ele);
		dplist.selectByIndex(Integer.parseInt(data.get(datanum+1)));
	}
}
