package steam.pages;

import framework.BasePage;
import org.apache.commons.lang3.text.WordUtils;
import org.openqa.selenium.By;
import framework.elements.Label;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ActionPage extends BasePage {

    private static final String PAGE_ACTION = "page.action";

    private static String uniqueElement = "//div[contains(@class, 'content_hub')]/h2[contains(text(), '%s')]";
    private static String lblTitle = locale.getLocalizedElementProperty(PAGE_ACTION);

    protected static String lblHoverAppBoxTitle;
    private String lblSpecialsMaxDiscount = "By.xpath//div[@id = 'specials_container']//div[contains(@class, 'discount_pct') and contains(text(), '%s')]";

    private Label lblHoverAppBox = new Label(By.xpath("//div[contains(@id, 'hover_app')]/h4"), "Chosen App Box");
    private Label lblSpecialsDiscountsList = new Label(By.xpath("//div[@id = 'specials_container']//div[contains(@class, 'discount_pct')]"), "Recommended Specials");

    public ActionPage() {
        super(By.xpath(String.format(uniqueElement, lblTitle)), "Action");
    }

    private String getHighestProfitInSpecials() {
        String textFromElement;
        String isolatedDiscount;
        String maxDiscount;
        int parseDiscountToInt;
        int i = -1;

        ArrayList<Integer> discounts = new ArrayList<>();
        List<WebElement> lblElements = lblSpecialsDiscountsList.findListOfElements();
        ArrayList<WebElement> lblDiscounts = new ArrayList<>(lblElements);

        for (WebElement elem : lblDiscounts) {
            textFromElement = elem.getText();
            isolatedDiscount = textFromElement.replace("-", "").replace("%", "");
            parseDiscountToInt = Integer.parseInt(isolatedDiscount);
            discounts.add(parseDiscountToInt);
        }
        maxDiscount = Collections.max(discounts).toString();
        return maxDiscount;
    }

    private void checkIfAgeCheckPageIsOpened() {
        try {
            logger.info(getLogProperty("locale.page.redirect"));
            new AgeCheckPage(true);
        } catch (NoSuchElementException e) {
            logger.info(getLogProperty("locale.page.noredirect"));
            new AppPage();
        }
    }

    public void chooseMostProfitableApp() {
        Label lblMostProfitableApp = new Label(lblSpecialsMaxDiscount, getHighestProfitInSpecials(), "Most profitable app");
        lblMostProfitableApp.moveToElement();
        lblHoverAppBoxTitle = lblHoverAppBox.getText();
        lblMostProfitableApp.clickAndWait();
        checkIfAgeCheckPageIsOpened();
    }
}
