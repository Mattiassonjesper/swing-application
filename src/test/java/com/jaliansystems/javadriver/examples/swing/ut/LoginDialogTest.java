package com.jaliansystems.javadriver.examples.swing.ut;

import net.sourceforge.marathon.javadriver.JavaDriver;
import net.sourceforge.marathon.javadriver.JavaProfile;
import net.sourceforge.marathon.javadriver.JavaProfile.LaunchMode;
import net.sourceforge.marathon.javadriver.JavaProfile.LaunchType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.List;

import static org.junit.Assert.*;


public class LoginDialogTest {

    private LoginDialog login;
    private WebDriver driver;

    @Before
    public void setUp() {
        login = new LoginDialog() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSuccess() {
            }

            @Override
            protected void onCancel() {
            }
        };
        SwingUtilities.invokeLater(() -> login.setVisible(true));
        JavaProfile profile = new JavaProfile(LaunchMode.EMBEDDED);
        profile.setLaunchType(LaunchType.SWING_APPLICATION);
        driver = new JavaDriver(profile);
    }

    @After
    public void tearDown() throws Exception {
        if (login != null)
            SwingUtilities.invokeAndWait(() -> login.dispose());
        if (driver != null)
            driver.quit();
    }

    @Test
    public void loginSuccess() {
        WebElement user = driver.findElement(By.cssSelector("text-field"));
        user.sendKeys("bob");
        WebElement pass = driver.findElement(By.cssSelector("password-field"));
        pass.sendKeys("secret");
        WebElement loginBtn = driver.findElement(By.cssSelector("button[text='Login']"));
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
        loginBtn.click();
        assertTrue(login.isSucceeded());
        //Simplified
        assertNotNull(login.getSize());
       // assertTrue(login.getSize() != null);
    }

    @Test
    public void loginCancel() {
        WebElement user = driver.findElement(By.cssSelector("text-field"));
        user.sendKeys("bob");
        WebElement pass = driver.findElement(By.cssSelector("password-field"));
        pass.sendKeys("secret");
        WebElement cancelBtn = driver.findElement(By.cssSelector("button[text='Cancel']"));
        cancelBtn.click();
        assertFalse(login.isSucceeded());
    }

    @Test
    public void loginInvalid() {
        WebElement user = driver.findElement(By.cssSelector("text-field"));
        user.sendKeys("bob");
        WebElement pass = driver.findElement(By.cssSelector("password-field"));
        pass.sendKeys("wrong");
        WebElement loginBtn = driver.findElement(By.cssSelector("button[text='Login']"));
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
        loginBtn.click();
        driver.switchTo().window("Invalid Login");
        driver.findElement(By.cssSelector("button[text='OK']")).click();
        driver.switchTo().window("Login");
        user = driver.findElement(By.cssSelector("text-field"));
        pass = driver.findElement(By.cssSelector("password-field"));
        assertTrue(user.getText().isEmpty());
        assertTrue(pass.getText().isEmpty());
        // assertEquals("", user.getText());
        // assertEquals("", pass.getText());
    }

    @Test
    public void checkTooltipText() {
        // Check that all the text components (like text fields, password
        // fields, text areas) are associated
        // with a tooltip
        List<WebElement> textComponents = driver.findElements(By.className(JTextComponent.class.getName()));
        for (WebElement tc : textComponents) {
            assertNotEquals(null, tc.getAttribute("toolTipText"));
        }
    }

    //uppgift 2
    @Test
    public void testLazyLogin() {
        WebElement user = driver.findElement(By.cssSelector("text-field"));
        WebElement pass = driver.findElement(By.cssSelector("password-field"));
        WebElement getLazyBtn = driver.findElement(By.cssSelector("button[text='LazyLogin']"));

        getLazyBtn.click();
        assertEquals("bob", user.getText());
        assertEquals("secret", pass.getText());
    }

    // Bonus
// I build.gradle filen under dependencies JUnit 4.12
    /*
    dependencies {

        // Use JUnit test framework
        testImplementation 'junit:junit:4.12'
        testImplementation 'com.jaliansystems:marathon-java-driver:5.2.5.0'
    }
*/
}
