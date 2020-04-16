package appium;

import java.io.IOException;

public class amazon extends AmazonImplementation{
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
	amazon app=new amazon();
	app.driverSetup();
	app.signin("srilakshmidudala@gmail.com", "dudala@1234");
	app.handleSigninPopups();
	app.productSearch("men shoes");
	app.navigateToPDP();
	app.addToCart();
	app.navigateTocart();
	app.navigateToDeliveryPage();
	//method implemented for country change if search is not working
	app.locationChange(); //it will change the location to  Australia
	app.ClearData();   //it will reset the app and clear the cache
	
	
	}
  
