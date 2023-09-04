package pageobject;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
import static org.testng.AssertJUnit.assertEquals;

public class ItensPage {
    SelenideElement txtPesquisaItem    = $(By.id("cphBody_Conteudo_txtProduto"));
    SelenideElement txtNovaPesquisa    = $(By.id("cphBody_Conteudo_txtPesquisa"));
    SelenideElement btnCancelaPesquisa = $(By.id("cphBody_Conteudo_btnLimpaBuscaPesquisa"));
    SelenideElement btnLimpaBusca      = $(By.id("cphBody_Conteudo_btnLimpaBusca"));
    SelenideElement btnConfirmar       = $(By.id("cphBody_Conteudo_btnConfirmar"));
    SelenideElement txtQtdItens        = $(By.id("cphBody_Conteudo_txtQuantidade"));
    SelenideElement optListaItens      = $(By.xpath("/html/body/form/div[6]/div[3]/div/div[2]/div[2]/div/table/tbody/tr[1]"));
    SelenideElement modalConfirmacao   = $(By.id("cphBody_Conteudo_Msg_lblMensagem"));
    SelenideElement btnFechaModal      = $(By.id("fechaPopup"));
    SelenideElement btnVoltar          = $(By.id("cphBody_lbtVoltar"));
    SelenideElement lblmsgSucesso      = $(By.id("cphBody_Conteudo_Msg_lblMensagem"));

    public void pesquisarItens(String codigo){
        txtPesquisaItem.setValue(codigo);
        txtPesquisaItem.pressEnter();
    }

    public void selecionarItens(String codItem){
        btnCancelaPesquisa.isEnabled();
        btnCancelaPesquisa.click();
        txtNovaPesquisa.setValue(codItem);
        txtNovaPesquisa.pressEnter();
        SelenideElement optLista =  $(By.xpath("//*/tr/td[contains(text(),'" + codItem + "')]"));
        optLista.isEnabled();
        optLista.click();
        btnConfirmar.isEnabled();
        btnConfirmar.click();
    }

    public void inserirItens(String qtdd){
        txtQtdItens.isEnabled();
        txtQtdItens.setValue(qtdd);
        optListaItens.click();
        btnConfirmar.isEnabled();
        btnConfirmar.click();
        verificarMensagemSucesso();
    }

    public void verificarMensagemSucesso(){
        modalConfirmacao.isEnabled();
        sleep(2000);
        assertEquals("Item lançado com sucesso!", lblmsgSucesso.getText());
        btnFechaModal.click();
    }

    public void novaInsercao(String codigo, String qtdd){
        btnLimpaBusca.isEnabled();
        btnLimpaBusca.click();
        pesquisarItens("ALTEPLASE");
        selecionarItens(codigo);
        inserirItens(qtdd);
    }
}