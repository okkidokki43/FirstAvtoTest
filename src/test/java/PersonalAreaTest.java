import com.github.javafaker.Faker;
import components.Header;
import components.popups.AuthorizationPopup;
import data.countrycities.ICityData;
import data.countrycities.RussiaCityData;
import data.fielddata.InputFieldData;
import data.genderdata.GenderData;
import data.language.EnglishLevelData;
import data.workformat.WorkFormatData;
import driverfactory.WebDriverFactory;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.PersonalAreaPage;

public class PersonalAreaTest {
    private Logger logger =  LogManager.getLogger(PersonalAreaTest.class);

    private Faker faker = new Faker();
    private WebDriver driver;

    public PersonalAreaTest() {
    }

    @BeforeEach
    public void init() {
        this.driver = (new WebDriverFactory("--start-maximized")).create();
        logger.info("Start driver");
    }

    @AfterEach
    public void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
        logger.info("Quit driver");
    }

    @Test
    public void addPersonalData() {
        (new MainPage(this.driver)).open("/");
        logger.info("Waiting ,ok");
        Header header = new Header(this.driver);
        header.clickLoginButton();

        logger.info("Check auth ");
        AuthorizationPopup authorizationPopup = new AuthorizationPopup(this.driver);
        authorizationPopup.popupShouldBeVisible();
        authorizationPopup.enterEmail();
        authorizationPopup.enterPassword();
        authorizationPopup.enterLoginButton();

        header.checkLogoUser();
        logger.info("Examination success > personal page");
        header.clickPersonalArea();

        PersonalAreaPage personalAreaPage = new PersonalAreaPage(this.driver);

        personalAreaPage.clearFieldsData(InputFieldData.FNAME, InputFieldData.FNAMELATIN, InputFieldData.LNAME,
                InputFieldData.LNAMELATIN, InputFieldData.BLOGNAME, InputFieldData.DATEOFBRTH);
        personalAreaPage.clearCountryEnglish();
        logger.info("Clear fields success");

        personalAreaPage.addData(faker.name().firstName(),InputFieldData.FNAME,
                InputFieldData.FNAMELATIN, InputFieldData.LNAME,
                InputFieldData.LNAMELATIN, InputFieldData.BLOGNAME);
        personalAreaPage.addData(faker.date().birthday().toInstant().atZone
                (ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern
                ("dd.MM.yyyy")), InputFieldData.DATEOFBRTH);
        ICityData[] cityData = RussiaCityData.values();
        logger.info("Add success");

        ICityData city = faker.options().nextElement(cityData);
        personalAreaPage.addCountry(city);
        personalAreaPage.addCity(city);
        personalAreaPage.addEnglishlevel(EnglishLevelData.FIRSTLEVEL);
        personalAreaPage.addWillingToRelocate(true);
        personalAreaPage.addWorkFormat(true, WorkFormatData.REMOTELY);
        personalAreaPage.addContactsOne(InputFieldData.SKYPE, faker.name().name(), 2);
        personalAreaPage.addContactsOne(InputFieldData.HABR, faker.name().name(), 3);
        personalAreaPage.addGender(GenderData.MALE);
        personalAreaPage.addData(faker.company().name(), InputFieldData.COMPANY);
        personalAreaPage.addData(faker.job().position(), InputFieldData.POSITION);
        logger.info("Add information success");

        personalAreaPage.clickSavePersonalArea();
        this.stopDriver();
        this.init();
        (new MainPage(this.driver)).open("/");
        header = new Header(this.driver);
        authorizationPopup = new AuthorizationPopup(this.driver);
        personalAreaPage = new PersonalAreaPage(this.driver);
        header.clickLoginButton();
        authorizationPopup.popupShouldBeVisible();
        authorizationPopup.enterEmail();
        authorizationPopup.enterPassword();
        authorizationPopup.enterLoginButton();
        header.checkLogoUser();
        logger.info("Successful");
        header.clickPersonalArea();
        personalAreaPage.checkPersonalArea(InputFieldData.FNAME, InputFieldData.FNAMELATIN, InputFieldData.LNAME,
                InputFieldData.LNAMELATIN, InputFieldData.BLOGNAME, InputFieldData.DATEOFBRTH);
        personalAreaPage.chechPersonalAreaData();
    }
}
