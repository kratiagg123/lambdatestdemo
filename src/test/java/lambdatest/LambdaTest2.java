package lambdatest;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LambdaTest2 {
	WebDriver driver = null;
	String username = "krati.aggarwal";
	String accessKey = "Kp6WU76IWaRWO0PxxL4lpCPGm5rxt4ckEbSx0gunQq8M467s88";

	String downloadDir = System.getProperty("user.dir") + "/tempdownload";

	@Parameters({ "target", "browser" })
	@BeforeTest
	public void setup(String target, String browser) {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + ".\\src\\test\\resources\\Driver3\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();

		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.prompt_for_download", false);
		options.setExperimentalOption("prefs", prefs);
		
		if (target.equals("local")) {
			driver = new ChromeDriver(options);
		} else {

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			capabilities.setCapability("platform", "Windows 10");
			capabilities.setCapability("browserName", browser);
			capabilities.setCapability("version", "92.0"); // If this cap isn't specified, it will just get the any
															// available one
			capabilities.setCapability("resolution", "1024x768");
			capabilities.setCapability("build", "First Test");
			capabilities.setCapability("name", "Sample Test");
			capabilities.setCapability("network", true); // To enable network logs
			capabilities.setCapability("visual", true); // To enable step by step screenshot
			capabilities.setCapability("video", true); // To enable video recording
			capabilities.setCapability("console", true); // To capture console logs
			options.merge(capabilities);

			try {
				driver = new RemoteWebDriver(
						new URL("https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub"), capabilities);
				((RemoteWebDriver)driver).setFileDetector(new LocalFileDetector());
				// driver = new ChromeDriver(options);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

	}

	@Test
	public void lambdatestassignmenet() throws InterruptedException, AWTException, IOException {

		// Open Browser and launch the URL

		driver.get("https://www.lambdatest.com/automation-demos/");
		driver.manage().window().maximize();
		WebElement logo = driver.findElement(By.xpath("//img[@alt=\"Logo\"]"));

		// verify the lamdaTest Logo
		Assert.assertTrue(logo.isDisplayed());
		WebElement seleniumPlaygroundText = driver.findElement(By.xpath("//h1[.='Selenium Playground']"));
		String playgroundText = seleniumPlaygroundText.getText();

		// verify the seleniumPlaygroundText
		Assert.assertEquals(playgroundText, "Selenium Playground", "Oppsss!! Text is not equal");

		// Enter valid login credentials

		driver.findElement(By.id("username")).sendKeys("lambda");
		driver.findElement(By.cssSelector("input[name='password']"));
		driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys("lambda123");
		driver.findElement(By.xpath("//button[.='Login']")).click();

		// Entered email id in email field and clicked on populate button

		driver.findElement(By.xpath("//input[@name=\"email\"]")).sendKeys("rahul.goyal@binmile.com");
		driver.findElement(By.xpath("//input[@value=\"Populate\"]")).click();

		// Handle Alert popup

		Alert alt = driver.switchTo().alert();
		alt.accept();

		// Select one random Radio button

		driver.findElement(By.xpath("//input[@value=\"Once in 3 months\"][1]")).click();

		// Select One checkbox

		driver.findElement(By.xpath("//input[@name=\"discounts\"][1]")).click();

		// Select payment mode from dropdown

		WebElement dropdown = driver.findElement(By.xpath("//select[@name=\"preferred-payment\"]"));
		Select sel = new Select(dropdown);
		sel.selectByIndex(2);

		// Select the chechbox for enable rating scale and feedback

		driver.findElement(By.xpath("//input[@name=\"tried-ecom\"]")).click();

		WebElement element = driver.findElement(By.xpath("//div[@aria-valuenow=\"50\"]"));
		Actions act = new Actions(driver);
		act.dragAndDropBy(element, 240, 0).click().perform();
		Thread.sleep(5000);

		// Open the page in new tab
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1)).get("https://www.lambdatest.com/selenium-automation");

		// download CI/CD image
		WebElement element1 = driver.findElement(By.xpath("//img[@title=\"Jenkins\"]"));
		// scrollPageTillElement(element1);
		String logoSRC = element1.getAttribute("src");
		URL imageURL = new URL(logoSRC);
//	     BufferedImage saveImage = ImageIO.read(imageURL);
//	     
//	     
//	     ImageIO.write(saveImage, "svg", new File(downloadDir+"/jenkins.svg"));
		FileUtils.copyURLToFile(imageURL, new File(downloadDir + "/jenkins.svg"), 60000, 60000);

		// navigate back to the parent window and click on upload image button
		driver.switchTo().window(tabs.get(0));

		// Upload Image
		WebElement element3 = driver.findElement(By.cssSelector("#file"));
		((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='visible';", element3);
		element3.sendKeys(downloadDir + "/jenkins.svg");

		// Alert pop-up

		Thread.sleep(2000);
		Alert alt1 = driver.switchTo().alert();
		String alertText = alt1.getText();
		Assert.assertEquals(alertText, "your image upload sucessfully!!", "Oppsss!! Text is not equal");

		alt1.accept();

		// Click on submit button

		WebElement submit = driver.findElement(By.xpath("//button[.='Submit']"));
		submit.click();
		WebElement thankYou = driver.findElement(By.xpath("//h1[.='Thank you!']"));
		String thankYoutext = thankYou.getText();
		Assert.assertEquals(thankYoutext, "Thank you!", "Oppsss!! Text is not equal");
		WebElement successfullySubmitText = driver
				.findElement(By.xpath("//p[.='You have successfully submitted the form.']"));
		String submitText = successfullySubmitText.getText();
		Assert.assertEquals(submitText, "You have successfully submitted the form.", "Oppsss!! Text is not equal");

		// Close browser tab and session

	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
