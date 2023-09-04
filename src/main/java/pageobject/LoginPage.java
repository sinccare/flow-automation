package pageobject;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    public SelenideElement txtCardNumber = $(By.id("txtCardNumber"));
    public SelenideElement txtPassword   = $(By.id("txtSenha"));
    public SelenideElement btnEnter      = $(By.id("btEntrar"));
    public SelenideElement labelCode     = $("#ChallengeFields > div > span");
    public SelenideElement txtCode       = $(By.xpath("//input[contains(@id, 'txtCode_')]"));


    public void fillCode(){
        String code;
        code = labelCode.getText();

        switch (code) {
            case "1:A":
                txtCode.sendKeys("V");
                btnEnter.click();
                break;
            case "1:B":
                txtCode.sendKeys("a");
                btnEnter.click();
                break;
            case "1:C":
                txtCode.sendKeys("5");
                btnEnter.click();
                break;
            case "1:D":
                txtCode.sendKeys("1");
                btnEnter.click();
                break;
            case "2:A":
                txtCode.sendKeys("s");
                btnEnter.click();
                break;
            case "2:B":
                txtCode.sendKeys("P");
                btnEnter.click();
            case "2:C":
                txtCode.sendKeys("s");
                btnEnter.click();
                break;
            case "2:D":
                txtCode.sendKeys("u");
                btnEnter.click();
                break;
            case "3:A":
                txtCode.sendKeys("e");
                btnEnter.click();
                break;
            case "3:B":
                txtCode.sendKeys("#");
                btnEnter.click();
                break;
            case "3:C":
                txtCode.sendKeys("D");
                btnEnter.click();
                break;
            case "3:D":
                txtCode.sendKeys("1");
                btnEnter.click();
                break;
            case "4:A":
                txtCode.sendKeys("r");
                btnEnter.click();
                break;
            case "4:B":
                txtCode.sendKeys("T");
                btnEnter.click();
                break;
            case "4:C":
                txtCode.sendKeys("i");
                btnEnter.click();
                break;
            case "4:D":
                txtCode.sendKeys("L");
                btnEnter.click();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + code);
        }
    }

    public void logar(){
        txtCardNumber.setValue("104795948");
        txtPassword.setValue("bhcl1234");
        btnEnter.click();
        fillCode();
    }
}
