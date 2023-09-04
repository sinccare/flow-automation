package pageobject;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {

    private SelenideElement menuSuprimentos    = $(By.xpath("//a[contains(text(), 'Suprimentos')]"));
    private SelenideElement moduloSuprimentos  = $(By.xpath("//a[contains(@title, 'Suprimentos Logística')]"));


    private WebDriver driver;

        public void acessarModuloSuprimentos(){
        Selenide.switchTo().window(1);
        menuSuprimentos.click();
        moduloSuprimentos.shouldBe(visible).click();
    }
}
