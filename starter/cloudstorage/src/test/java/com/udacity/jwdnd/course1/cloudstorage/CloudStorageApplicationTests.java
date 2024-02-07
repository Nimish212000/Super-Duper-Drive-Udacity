package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.nio.file.Paths;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private static ChromeDriverService service;

	public String baseURL;

	@BeforeAll
	static void beforeAll() throws IOException {
		service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File("C:\\Users\\user\\Desktop\\Udacity Course\\ChromeDriver\\chromedriver-win64\\chromedriver.exe"))
				.usingAnyFreePort()
				.build();
		service.start();
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver(service);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void testSignupLoginLogout() {

		WebDriverWait wait = new WebDriverWait(driver, 10);
		String username = "newuser";
		String password = "password123";

		driver.get("http://localhost:" + this.port + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("John", "Doe", username, password);
		driver.get("http://localhost:" + this.port + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebElement homeTab = driver.findElement(By.id("nav-home-tab"));
		wait.until(ExpectedConditions.titleContains("Home"));
		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		WebElement logoutButton = driver.findElement(By.id("logout-button"));
		logoutButton.click();

		WebElement loginTab = driver.findElement(By.id("nav-login-tab"));
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

	}


	@Test
	@Order(2)
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/

		assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	@Order(3)
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	@Order(4)
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	@Order(5)
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	@Order(6)
	public void testFileUploadAndDownload() {
		String username = "newuser";
		String password = "password123";

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		String fileName = "61109.png";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();

		WebElement resultTab = driver.findElement(By.id("home-link"));
		resultTab.click();

		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		webDriverWait.until(ExpectedConditions.titleContains("Home"));


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("View")));
		WebElement downloadButton = driver.findElement(By.id("View"));
		downloadButton.click();

		String downloadFolderPath = "C:\\Users\\user\\Downloads";
		String expectedFileName = "61109.png";
		String filePath = downloadFolderPath + "\\" + expectedFileName;

		webDriverWait.until((WebDriver webDriver) -> {
			assert webDriver != null;
			return Files.exists(Paths.get(filePath));
		});

		if (Files.exists(Paths.get(filePath))) {
			System.out.println("File download successful.");
		} else {
			System.out.println("File download failed.");
		}
	}

	@Test
	@Order(7)
	public void testDeleteFile() {
		String username = "newuser";
		String password = "password123";

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Delete")));
		WebElement deleteFile = driver.findElement(By.id("Delete"));
		deleteFile.click();

		WebElement resultTab = driver.findElement(By.id("home-link"));
		resultTab.click();

		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		boolean isFilePresent = !driver.findElements(By.id("file-on-page")).isEmpty();
		assertFalse(isFilePresent);
	}

	@Test
	@Order(8)
	public void testCreateAndVerifyNote() {
		String username = "newuser";
		String password = "password123";

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		
		WebElement addNoteButton = driver.findElement(By.id("add-note"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note")));
		addNoteButton.click();
		
		WebElement noteTitleField = driver.findElement(By.id("note-title"));
		WebElement noteDescriptionField = driver.findElement(By.id("note-description"));
		WebElement saveNoteButton = driver.findElement(By.id("noteSubmitbtn"));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		noteTitleField.sendKeys("Test Note Title");
		noteDescriptionField.sendKeys("Test Note Description");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSubmitbtn")));
		saveNoteButton.click();
		
		WebElement resultTab = driver.findElement(By.id("home-link"));
		resultTab.click();

		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesnewTab = driver.findElement(By.id("nav-notes-tab"));
		notesnewTab.click();

		WebElement noteTitleOnPage = driver.findElement(By.id("note-title-on-page"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-on-page")));
		WebElement noteDescriptionOnPage = driver.findElement(By.id("note-description-on-page"));
		assertEquals("Test Note Title", noteTitleOnPage.getText());
		assertEquals("Test Note Description", noteDescriptionOnPage.getText());
	}

	@Test
	@Order(9)
	public void testEditNote() {
		String username = "newuser";
		String password = "password123";
		
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-btn")));
		WebElement editNoteButton = driver.findElement(By.id("edit-btn"));
		editNoteButton.click();

		WebElement noteTitleField = driver.findElement(By.id("note-title"));
		WebElement noteDescriptionField = driver.findElement(By.id("note-description"));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		noteTitleField.clear(); 
		noteTitleField.sendKeys("Updated Note Title");
		noteDescriptionField.clear(); 
		noteDescriptionField.sendKeys("Updated Note Description");

		WebElement saveNoteButton = driver.findElement(By.id("noteSubmitbtn"));
		saveNoteButton.click();

		WebElement resultTab = driver.findElement(By.id("home-link"));
		resultTab.click();

		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesupdateTab = driver.findElement(By.id("nav-notes-tab"));
		notesupdateTab.click();
		
		WebElement noteTitleOnPage = driver.findElement(By.id("note-title-on-page"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-on-page")));
		WebElement noteDescriptionOnPage = driver.findElement(By.id("note-description-on-page"));
		assertEquals("Updated Note Title", noteTitleOnPage.getText());
		assertEquals("Updated Note Description", noteDescriptionOnPage.getText());
	}

	@Test
	@Order(10)
	public void testDeleteNote() {
		String username = "newuser";
		String password = "password123";

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesnewTab = driver.findElement(By.id("nav-notes-tab"));
		notesnewTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-btn")));
		WebElement deleteNoteButton = driver.findElement(By.id("delete-btn"));
		deleteNoteButton.click();

		WebElement resultTab = driver.findElement(By.id("home-link"));
		resultTab.click();

		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesdeleteTab = driver.findElement(By.id("nav-notes-tab"));
		notesdeleteTab.click();

		boolean isNotePresent = !driver.findElements(By.id("note-title-on-page")).isEmpty();
		assertFalse(isNotePresent);
	}

	@Test
	@Order(11)
	public void testCreateCredentialsAndVerifyEncryption() {
		String username = "newuser";
		String password = "password123";

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsTab = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential")));
		WebElement addCredentialButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("add-credential")));
		addCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		driver.findElement(By.id("credential-url")).sendKeys("example.com");
		driver.findElement(By.id("credential-username")).sendKeys("your_username");
		driver.findElement(By.id("credential-password")).sendKeys("your_password");
		driver.findElement(By.id("credential-submit")).click();

		WebElement resultTab = driver.findElement(By.id("home-link"));
		resultTab.click();

		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsnewTab = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		credentialsnewTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url-on-page")));
		WebElement credentialUrlElement = driver.findElement(By.id("credential-url-on-page"));
		assertEquals("example.com", credentialUrlElement.getText());
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password-on-page")));
		WebElement credentialPasswordElement = driver.findElement(By.id("credential-password-on-page"));
		assertFalse(credentialPasswordElement.getText().startsWith("Encrypted"));

	}

	@Test
	@Order(12)
	public void testViewAndEditCredentials() throws InterruptedException {
		String username = "newuser";
		String password = "password123";

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsTab = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editcredential-btn")));
		driver.findElement(By.id("editcredential-btn")).click();

		WebElement passwordElement = driver.findElement(By.id("credential-password"));
		String passwordValue = passwordElement.getAttribute("value");
		assertEquals("your_password", passwordValue);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		driver.findElement(By.id("credential-url")).clear();
		driver.findElement(By.id("credential-url")).sendKeys("edited_example.com");
		driver.findElement(By.id("credential-username")).clear();
		driver.findElement(By.id("credential-username")).sendKeys("edited_username");
		System.out.println(driver.findElement(By.id("credential-password")).getText());
		driver.findElement(By.id("credential-password")).clear();
		driver.findElement(By.id("credential-password")).sendKeys("edited_password");
		driver.findElement(By.id("credential-submit")).click();

		WebElement resultTab = driver.findElement(By.id("home-link"));
		resultTab.click();

		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsnewTab = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		credentialsnewTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url-on-page")));
		WebElement credentialUrlElement = driver.findElement(By.id("credential-url-on-page"));
		assertEquals("edited_example.com", credentialUrlElement.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password-on-page")));
		WebElement editedCredentialPasswordElement = driver.findElement(By.id("credential-password-on-page"));
		assertFalse(editedCredentialPasswordElement.getText().startsWith("Encrypted"));
	}

	@Test
	@Order(13)
	public void testDeleteCredentials() {
		String username = "newuser";
		String password = "password123";

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsTab = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		credentialsTab.click();

		WebElement deleteCredentialButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("delete-credential")));
		deleteCredentialButton.click();

		WebElement resultTab = driver.findElement(By.id("home-link"));
		resultTab.click();

		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialsnewTab = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		credentialsnewTab.click();
		assertFalse(driver.findElements(By.id("credential-url-on-page")).size() > 0);
	}
}







