package stepDef;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.factory.DriverFactory;
import com.util.ConfigReader;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class ApplicationHooks {

	private DriverFactory driverFactory; // to initialise the driver
	private WebDriver driver; // to store the returned driver
	private ConfigReader configReader; //to read the config properties like url and browser type
	Properties prop; //to store the properties and pass them to the driver initiliastion class

	
	//to load the proprties from the config file into the properties object
	@Before(order = 0)
	public void getProperty() {
		configReader = new ConfigReader();
		prop = configReader.init_prop();
	}
	
	
//to pass the properties into the driver object and store the driver that was returned
	@Before(order = 1)
	public void launchBrowser() {
		String browserName = prop.getProperty("browser");
		System.out.println("we are inside the launch browser method");
		driverFactory = new DriverFactory();
		driver = driverFactory.init_driver(browserName);
		
	}

	
	//after hook with order 0 runs in the last ...opposite to the before hook ..to quit the driver
	@After(order = 0)
	public void quitBrowser() {
		driver.quit();
	}

	
	//hook to take the screenshot ..type cast the driver to TakeScreenshot
	@After(order = 1)
	public void tearDown(Scenario scenario) {
		if (scenario.isFailed()) {
			// take screenshot:
			String screenshotName = scenario.getName().replaceAll(" ", "_");
			byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			scenario.attach(sourcePath, "image/png", screenshotName);

		}
	}

}