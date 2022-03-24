package framework;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import framework.elements.Label;
import org.openqa.selenium.NoSuchElementException;

import java.util.Date;

import static io.qameta.allure.Allure.getLifecycle;
import static org.apache.commons.logging.LogFactory.getLog;

public class BasePage {

    protected String pageTitle;
    protected String fullPageName;

    private By pageLocator;
    private boolean isRedirected = false;
    protected Browser browser = Browser.getInstance();
    protected static Logger logger = Logger.getInstance();
    protected static Localization locale = new Localization();

    public static String translateText = "translate(text(), ' ', ' ')";

    public BasePage(final By pageLocator, final String pageTitle) {
        browser.waitForPageToLoad();
        initPage(pageLocator, pageTitle);
        assertCorrectPage();
    }

    public BasePage(final By pageLocator, final String pageTitle, final boolean isRedirected) {
        browser.waitForPageToLoad();
        this.isRedirected = isRedirected;
        initPage(pageLocator, pageTitle);
        assertCorrectPage();
    }

    @Step("Opening {pageTitle} page...")
    private void initPage(final By pageLocator, final String pageTitle) {
        getLifecycle().updateStep(step -> {
                    step.getParameters().remove(0);
                    step.getParameters().remove(0);
                });
        this.pageLocator = pageLocator;
        this.pageTitle = pageTitle;
        fullPageName = String.format(getLog("locale.page") + " '%1$s'", this.pageTitle);
    }

    @Step("Making sure that the open page is correct...")
    private void assertCorrectPage() {
        long initTime = new Date().getTime();
        logger.info(String.format(getLogProperty("locale.page.load"), pageTitle));
        checkIsRedirected();
        try {
            long timeToOpen = new Date().getTime() - initTime;
            logger.info(getLogProperty("locale.page.loaded") + String.format(" %s mills.", timeToOpen));
            logger.info(String.format(getLogProperty("locale.page.correct"), pageTitle));
        } catch (Throwable exc){
            logger.fatal(getLogProperty("locale.page.incorrect"), exc);
        }
    }

    private void checkIsRedirected() {
        Label uniqueElement = new Label(pageLocator, pageTitle);
        try {
            uniqueElement.waitForElementToBePresent();
        } catch (NoSuchElementException exc) {
            if (isRedirected) {
                isRedirected = false;
                throw new NoSuchElementException("No redirection to another page.");
            }
            logger.fatal(getLogProperty("locale.page.incorrect"), exc);
        }
    }

    protected static String getLogProperty(final String key) {
        return Logger.getLogProperty(key);
    }
}
