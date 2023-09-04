package pageobject;


import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class SuprimentosLogisticaPage {
    SelenideElement cbEmpresa           = $(By.xpath("//div[@id='cphBody_Conteudo_ddlEmpresa_chosen']/a/span"));
    SelenideElement optEmpresa          = $(By.xpath("//li[contains(text(), 'UNIDADE DE PRONTO ATENDIMENTO - PA MARIA DIRCE')]"));
    SelenideElement cbRecurso           = $(By.xpath("//div[@id='cphBody_Conteudo_ddlRecurso_chosen']/a/span"));
    SelenideElement optRecurso          = $(By.xpath("//div[@id='cphBody_Conteudo_ddlRecurso_chosen']/div/ul/li[3]"));
    SelenideElement menuLateral         = $(By.xpath("//div[@id='menuRecolhido']/div/div"));
    SelenideElement submenuPresDireta   = $(By.xpath("//a[contains(text(),'Prescrição Direta')]"));

    public void preencherEmpresaRecurso(){
        cbRecurso.click();
        cbEmpresa.click();
        optEmpresa.shouldBe(visible).click();
        sleep(2000);
        cbRecurso.click();
        optRecurso.isDisplayed();
        optRecurso.click();
    }
    public void acessarPrescricaoDireta(){
        preencherEmpresaRecurso();
        menuLateral.click();
        submenuPresDireta.click();
    }
}
