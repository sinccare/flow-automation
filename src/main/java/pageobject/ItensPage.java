package pageobject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class ItensPage {
    private ExtentTest test;


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
    SelenideElement lblMsgAviso        = $(By.id("cphBody_Conteudo_Msg_lblMensagem"));

    public void pesquisarItens(String codigo){
        txtPesquisaItem.setValue(codigo);
        txtPesquisaItem.pressEnter();
    }

    public void selecionarItens(String codItem) throws Exception {
        btnCancelaPesquisa.isEnabled();
        btnCancelaPesquisa.click();
        txtNovaPesquisa.setValue(codItem);
        txtNovaPesquisa.pressEnter();
        SelenideElement optLista =  $(By.xpath("//*/tr/td[contains(text(),'" + codItem + "')]"));
        if(lblMsgAviso.isEnabled()){
            test.log(Status.FAIL,"A opição não foi carregada na tela");

        }else
        {
            optLista.click();
            btnConfirmar.isEnabled();
            btnConfirmar.click();
        }
    }

    public void inserirItens(String qtdd) throws Exception {
        txtQtdItens.isEnabled();
        txtQtdItens.setValue(qtdd);
        optListaItens.click();
        btnConfirmar.isEnabled();
        btnConfirmar.click();
        verificarMensagemSucesso();
    }

    public void verificarMensagemSucesso() throws Exception {
        if(lblmsgSucesso.getText() == "Item lançado com sucesso!"){
            modalConfirmacao.isEnabled();
            sleep(2000);
            btnFechaModal.click();
        }else{
            test.log(Status.FAIL, "Ocorreu um erro ao inserir o item");
        }
    }

    public void novaInsercao(String codigo, String qtdd) throws Exception {
        btnLimpaBusca.isEnabled();
        btnLimpaBusca.click();
        pesquisarItens("ALTEPLASE");
        selecionarItens(codigo);
        inserirItens(qtdd);
    }
}