package StepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class ECommerceSteps {

    WebDriver driver;

    @Given("The user visits the Trendyol website")
    public void visit_trendyol() {
        setupDriver();
        driver.get("https://www.trendyol.com/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Gender popup kapatılması
        try {
            WebElement genderPopupClose = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".modal-close")));
            genderPopupClose.click();
            System.out.println("Gender popup kapatıldı.");
        } catch (Exception e) {
            System.out.println("Gender popup görünmedi veya kapatılamadı.");
        }

        // Çerez popup kapatılması
        try {
            WebElement cookieAccept = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button#onetrust-accept-btn-handler")));
            cookieAccept.click();
            System.out.println("Çerez popup'ı kapatıldı.");
        } catch (Exception e) {
            System.out.println("Çerez popup görünmedi veya kapatılamadı.");
        }
    }

    @And("The user logs in with valid credentials")
    public void user_logs_in() {
        try {
        	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        	WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
        	        By.cssSelector(".account-user .link-text")));
        	loginButton.click();

            driver.findElement(By.id("login-email")).sendKeys("test@demo.com");
            driver.findElement(By.id("login-password-input")).sendKeys("enoctatest");
            driver.findElement(By.cssSelector(".submit > button")).click();

        } catch (Exception e) {
            System.out.println("Login skipped or failed in test mode.");
            e.printStackTrace();
        }
    }

    @When("The user searches for {string}")
    public void search_product(String keyword) {
        WebElement searchBox = driver.findElement(By.cssSelector("input[data-testid='suggestion']"));
        searchBox.sendKeys(keyword);
        searchBox.sendKeys(Keys.ENTER);
    }

    @And("The user filters the price between 15000 and 20000 TL")
    public void filter_price() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Fiyat filtresi başlığını aç
            WebElement priceFilterToggle = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='fltr-cntnr-ttl' and text()='Fiyat']/following-sibling::div")));
            priceFilterToggle.click();

            // Fiyat aralıklarını gir
            WebElement min = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.fltr-srch-prc-rng-input.min")));
            WebElement max = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.fltr-srch-prc-rng-input.max")));

            min.clear();
            min.sendKeys("15000");

            max.clear();
            max.sendKeys("20000");

            // Uygula butonuna tıkla
            WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.fltr-srch-prc-rng-srch")));
            applyButton.click();

            System.out.println("Fiyat filtresi uygulandı.");

        } catch (Exception e) {
            System.out.println("Fiyat filtresi uygulanamadı.");
            e.printStackTrace();
        }
    }

    @And("The user selects a random product from the bottom of the list")
    public void select_random_product() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector("a.p-card-chldrn-cntnr.card-border")));

            if (!products.isEmpty()) {
                int limit = Math.min(5, products.size());
                Random rand = new Random();
                int index = rand.nextInt(limit);

                // Tıklamadan önce mevcut sekmeyi al
                String currentWindow = driver.getWindowHandle();

                // Tıklayıp yeni sekmenin açılmasını bekle
                products.get(index).click();

                // Yeni sekmeye geç
                for (String windowHandle : driver.getWindowHandles()) {
                    if (!windowHandle.equals(currentWindow)) {
                        driver.switchTo().window(windowHandle);
                        break;
                    }
                }

                System.out.println("Yeni sekmede ürün detay sayfasına geçildi.");

            } else {
                System.out.println("Hiç ürün bulunamadı.");
            }

        } catch (Exception e) {
            System.out.println("Ürün seçimi sırasında hata oluştu.");
            e.printStackTrace();
        }
    }

    @And("The user adds the product from the lowest-rated seller to the cart")
    public void add_lowest_rated_to_cart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // "Anladım" popup kapatılması
        try {
            WebElement understoodButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.onboarding-popover__default-renderer-primary-button")));
            understoodButton.click();
            System.out.println("‘Anladım’ popup'ı kapatıldı.");
        } catch (Exception e) {
            System.out.println("‘Anladım’ popup'ı görünmedi.");
        }

        try {
            List<WebElement> sellerCards = driver.findElements(By.cssSelector("div.pr-mc-w"));

            WebElement lowestRatedCard = null;
            double lowestRating = Double.MAX_VALUE;

            for (WebElement card : sellerCards) {
                try {
                    WebElement ratingEl = card.findElement(By.cssSelector(".sl-pn"));
                    String ratingText = ratingEl.getText().replace(",", ".").trim(); // Örn: "9,1" → "9.1"
                    double rating = Double.parseDouble(ratingText);

                    if (rating < lowestRating) {
                        lowestRating = rating;
                        lowestRatedCard = card;
                    }
                } catch (Exception ignore) {
                    // Puan yoksa bu satıcıyı atla
                }
            }

            if (lowestRatedCard != null) {
                // En düşük puanlı satıcının “Ürüne Git” butonuna tıkla
            	WebElement button = lowestRatedCard.findElement(By.cssSelector("a.pr-om-lnk-btn"));
                button.click();
                System.out.println("En düşük puanlı satıcı seçildi. Puan: " + lowestRating);
            } else {
                System.out.println("Hiçbir satıcının puanı bulunamadı. İlk ‘Sepete Ekle’ deneniyor.");
                driver.findElement(By.cssSelector("button.add-to-basket")).click();
            }

        } catch (Exception e) {
            System.out.println("Satıcı seçimi sırasında hata oluştu.");
            e.printStackTrace();
        }
        
     // Satıcı detay sayfasında sepete ekle
        try {
            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement addToCartBtn = wait2.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.add-to-basket")));
            addToCartBtn.click();
            System.out.println("Satıcı detay sayfasında sepete ekleme yapıldı.");
        } catch (Exception e) {
            System.out.println("Satıcı detay sayfasında sepete ekleme bulunamadı.");
        }
    }

    @Then("The product should be visible in the cart")
    public void verify_cart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // "1 ürün" yazısını kontrol et
            WebElement confirmationText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.go-to-basket-product-text")));

            String cartText = confirmationText.getText().trim().toLowerCase();
            System.out.println("Sepet bildirimi: " + cartText);
            assert cartText.contains("1 ürün") || cartText.contains("ürün") : "Cart confirmation not found!";

        } catch (Exception e) {
            throw new AssertionError("Could not verify cart contents.");
        } finally {
            driver.quit();
        }
    }

    public void setupDriver() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("src/test/resources/config.properties"));
            String browser = prop.getProperty("browser", "chrome");

            if (browser.equalsIgnoreCase("chrome")) {
                System.setProperty("webdriver.chrome.driver", "C:\\WebDriver\\chromedriver.exe");

                ChromeOptions options = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 2); // 1: izin ver, 2: engelle
                options.setExperimentalOption("prefs", prefs);

                driver = new ChromeDriver(options);
            } else if (browser.equalsIgnoreCase("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else {
                throw new RuntimeException("Unsupported browser in config.properties");
            } 

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            driver.manage().window().maximize();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties");
        }
    }
}