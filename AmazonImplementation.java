package appium;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Duration;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;

class AmazonImplementation{
	Properties obj = new Properties();
	AndroidDriver<WebElement> driver=null;
	public void driverSetup() throws IOException {
	 FileInputStream objfile = new FileInputStream(System.getProperty("user.dir")+"\\src\\com\\ObjectRepository\\AmazonObjectRepo.properties");
	    obj.load(objfile);
		DesiredCapabilities capability=new DesiredCapabilities();
		//capabilities to launch amazon native application
		capability.setCapability("deviceName", "Redmi Note 5 Pro");
		capability.setCapability("platformVersion", "8.1.0");
		capability.setCapability("PlatformName", "Android");
		capability.setCapability("appPackage", "com.amazon.mShop.android.shopping");
		capability.setCapability("appActivity", "com.amazon.mShop.splashscreen.StartupActivity");
		driver=new AndroidDriver<WebElement>(new URL("http://192.168.43.147:4723/wd/hub"), capability);
	    driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	
	public WebElement getElement(String Element)
	{
		String Elementtype=null;
		if(obj.getProperty(Element).startsWith("//")||obj.getProperty(Element).startsWith("(//"))
			Elementtype="xpath";
		else
			Elementtype="id";
		WebElement element = null;
		switch(Elementtype) {
			case "id":   element=driver.findElementById(obj.getProperty(Element));
			             break;
			case "xpath":element=driver.findElementByXPath(obj.getProperty(Element));
			             break;
	}
		return element;
	}
	public void signin(String email, String pass) throws InterruptedException {
		boolean signin_required= getElement("Skip_Signin_Button").isDisplayed();
	    if(signin_required)
	    {
	    	getElement("ExistingCustomer").click();
	    	boolean isSigninPageOpened=getElement("Signin_Email").isDisplayed();
	    	Assert.assertTrue(isSigninPageOpened);
	    	getElement("Signin_Email").sendKeys(email);
	    	getElement("SigninPage_Countinue").click();
	    	boolean isPassKeyPageOpened=getElement("Signin_PasswordPage_Verify").isDisplayed();
	    	Assert.assertTrue(isPassKeyPageOpened);
	    	getElement("Signin_PassKey").sendKeys(pass);
	    	getElement("SignIn_Button").click();
	    	Thread.sleep(10000);
	    }
	}
	public void handleSigninPopups() {
		boolean HomePage_Popup=getElement("HomePage_Popup").isDisplayed();
    	if(HomePage_Popup)
    		getElement("HomePage_Popup_Close").click();	
	}
	public void productSearch(String search) {
		getElement("HomePage_Search").click();
		getElement("HomePage_Search").sendKeys(search);
		//we can't able to take id for keyboard in android, so using touchactions to click on search.
	    TouchAction action=new TouchAction(driver);
	    action.tap(new PointOption().withCoordinates(1075, 2020)).perform();
	    Assert.assertTrue(getElement("Search_Results_Check").isDisplayed());
	}
	public void navigateToPDP() {
		getElement("PMP_RandomProduct").click();
		//handleSigninPopups();
		Assert.assertTrue(getElement("PDP_VerifyName").isDisplayed());
		
	}
	public void addToCart() {
		Assert.assertTrue(getElement("PDP_SelectSize_text").isDisplayed());
		getElement("PDP_SelectSize").click();
		int height=driver.manage().window().getSize().height;
		int length=driver.manage().window().getSize().width;
		TouchAction act=new TouchAction(driver);
	    act.longPress(new PointOption().withCoordinates(550, 2000)).moveTo(new PointOption().withCoordinates(550,1000)).release().perform();
		getElement("PDP_AddToCart_Button").click();
	}
	public void navigateTocart() {
		getElement("AddToCart_Bucket").click();
		Assert.assertTrue(getElement("AddToCartPage_Verify").isDisplayed());
	}
	public void navigateToDeliveryPage() {
		getElement("Verify_DeliveryPage").click();
		Assert.assertTrue(getElement("AddToCartPage_Verify").isDisplayed());
		getElement("DeliveryAddress").click();
		Assert.assertTrue(getElement("DeliveryAddress_Continue").isDisplayed());
	}
	public void locationChange() throws InterruptedException
	{
		getElement("hamburger_menu").click();
		getElement("hamburgerMenu_Settings").click();
		getElement("ChangeRequest").click();
		Thread.sleep(10000);
		getElement("ChangeRegion").click();
		getElement("PDP_SelectSize").click();
		getElement("Country_change_done").click();
	}
	public void ClearData() throws InterruptedException {
		driver.resetApp();
		driver.removeApp("io.appium.unlock");
		Thread.sleep(5000);
		driver.removeApp("io.appium.settings");
	}
}

