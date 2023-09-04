package pageobject;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PrescricaoDiretaPage {
    SelenideElement txtPaciente             = $(By.id("cphBody_Conteudo_txtPaciente"));
    SelenideElement btnPesquisaPaciente     = $(By.id("cphBody_Conteudo_btnPesquisa"));
    SelenideElement loading                 = $(By.xpath("//*[@id='cphBody_Conteudo_UpdateProg1']/div/div/div[contains(text(), 'Carregando ...')]"));
    SelenideElement btnInserirItem          = $(By.id("cphBody_Conteudo_btnInserir"));
    SelenideElement btnConfirmaPaciente     = $(By.xpath("//div[@id='box_geral']/div[2]/div[3]/ul/li/input[contains(@value, 'Confirmar')]"));


    public void selecionarPaciente(String nome){
        txtPaciente.setValue(nome);
        btnPesquisaPaciente.click();
        loading.shouldBe(disappear);
        Selenide.$(By.xpath("//*/tr/td[contains(text(),'" + nome + "')]")).click();
        loading.shouldBe(disappear);
        btnConfirmaPaciente.shouldBe(visible).click();
        btnInserirItem.shouldBe(visible).click();
    }

//    public void acessarInsercaoItens(){
//        btnInserirItem.shouldBe(visible).click();
//    }
}
