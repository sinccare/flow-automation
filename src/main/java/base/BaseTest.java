package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dto.ItensInseridosDto;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pageobject.*;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.google.common.base.Throwables.getStackTraceAsString;
import static configuration.SelenideConfiguration.configurationSelenide;
import static constants.GenericConstants.END_DATE;
import static constants.GenericConstants.START_DATE;

public abstract class BaseTest {
    public LoginPage login;
    public HomePage home;
    public SuprimentosLogisticaPage suprimentos;
    public PrescricaoDiretaPage prescricaoDireta;
    public ItensPage itens;

    private List<ItensInseridosDto> itensInseridos = new ArrayList<>();

    public ExtentReports extent;
    public ExtentTest test;

    @BeforeSuite
    public void setUpExtentReport() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("relatorios/RELATORIO-INSERCAO.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @BeforeMethod
    public void createTest() {
        test = extent.createTest("Inserção de Itens no sistema Siss");
    }


    @BeforeMethod
    public void before() {
        configurationSelenide();
        clearBrowserCookies();
        login = new LoginPage();
        home = new HomePage();
        suprimentos = new SuprimentosLogisticaPage();
        prescricaoDireta = new PrescricaoDiretaPage();
        itens = new ItensPage();
    }

    private boolean jsonPathIsNull(JsonPath jsonPath) {
        return jsonPath.getString("items[0].custom_code") == null;
    }

    protected void consultarDados() throws  Exception {
        String ultimoId = Utils.lerArquivo("ultimoItemInserido", "last_id");

        try {
            JsonPath jsonPath = Utils.consultarMassaDeDados(START_DATE, END_DATE, ultimoId);


            if (jsonPathIsNull(jsonPath)) {
                Utils.criarArquivoUltimoID(ultimoId);
                Utils.criarArquivo(null, ultimoId, null);
                test.log(Status.WARNING, "A consulta não retornou itens!");
                extent.flush();

            } else {
                processarDadosConsulta(jsonPath, ultimoId);
            }
        } catch (IOException e) {
            String errorMessage = "Ocorreu um erro durante a consulta de dados:";
            test.log(Status.FATAL, errorMessage);
            test.log(Status.FATAL, "Tipo de Exceção: " + e.getClass().getName());
            test.log(Status.FATAL, "Mensagem de Erro: " + e.getMessage());
            test.log(Status.FATAL, "Stack Trace: " + getStackTraceAsString(e));
            extent.flush();
            throw new Exception("Erro ao consultar dados", e);
        }
    }
    private void processarDadosConsulta(JsonPath jsonPath, String ultimoId) throws Exception {
        Utils.criarArquivo(
                jsonPath.getString("items[0].custom_code"),
                jsonPath.getString("items[0].id"),
                jsonPath.getString("items[0].quantity"));

        logarEConfigurarPaciente();
        Utils.criarArquivoUltimoID(ultimoId);
    }

    private void logarEConfigurarPaciente() {
        open("https://guarulhoshospitalar.sissonline.com.br/Abertura/Login.aspx");
        login.logar();
        home.acessarModuloSuprimentos();
        suprimentos.acessarPrescricaoDireta();
        prescricaoDireta.selecionarPaciente("PACIENTE TESTE");
    }

    private void selecionarEPreencherDadosItemASerInserido(String lastCustomCode, String lastQuantity, String bkpId) throws Exception {
       try {
           itens.pesquisarItens("ALTEPLASE");
           itens.novapesquisaItem(lastCustomCode);
           SelenideElement optLista = $(By.xpath("//*/tr/td[contains(text(),'" + lastCustomCode + "')]"));

           if (optLista.is(visible)) {
               optLista.click();
               itens.btnConfirmar.isEnabled();
               itens.btnConfirmar.click();
               adicionarItemInserido(bkpId);
               Utils.criarArquivoUltimoID(bkpId);
               criarArquivoYAML();
               consultarDados();
           }else{
               test.log(Status.ERROR, "Item não acessível");
               extent.flush();
               closeWindow();
               throw new Exception();
           }
           itens.inserirItens(lastQuantity);


       }catch (Exception e){
           Utils.criarArquivo(null, Utils.lerArquivo("massaDeDados", "last_id"), null);
           Utils.criarArquivoUltimoID(bkpId);
           Selenide.closeWindow();
           test.log(Status.ERROR, "Ocorreu um erro durante a pesquisa do item, erro: " + e.getMessage());
           extent.flush();
           throw new Exception(e.getMessage());
       }

    }
    protected void inserirSuprimentos() throws Exception {
        String bkpId = null;
        try {
            String lastCustomCode = Utils.lerArquivo("massaDeDados", "last_custom_code");
            String lastQuantity = Utils.lerArquivo("massaDeDados", "last_quantity");
            bkpId = Utils.lerArquivo("massaDeDados", "last_id");

            selecionarEPreencherDadosItemASerInserido(lastCustomCode, lastQuantity, bkpId);

        } catch (Exception e) {
            Utils.criarArquivo(null, Utils.lerArquivo("massaDeDados", "last_id"), null);
            Utils.criarArquivoUltimoID(bkpId);
            Selenide.closeWindow();
            test.log(Status.ERROR, "Ocorreu um erro durante a execução do fluxo, erro: " + e.getMessage());
            extent.flush();
            throw new Exception(e.getMessage());
        }
    }
    protected void novaInsercao() throws Exception {
        String bkpId = Utils.lerArquivo("ultimoItemInserido", "last_id");
        try {
            String lastIdExecution = Utils.lerArquivo("ultimoItemInserido", "last_id");

            while (true) {
                JsonPath jsonPath = Utils.consultarMassaDeDados(START_DATE, END_DATE, lastIdExecution);

                if (jsonPathIsNull(jsonPath)) {
                    Utils.criarArquivo(null, Utils.lerArquivo("massaDeDados", "last_id"), null);
                    Selenide.closeWindow();
                    test.log(Status.INFO, "Após nova consulta, nenhum item foi retornado!");
                    test.log(Status.INFO, "Lista de item(s) inserido(s): " +
                                                 Utils.lerArquivo("itens_inseridos", "id"));
                    extent.flush();
                    break;
                } else {
                    processarNovaInsercao(jsonPath);
                }
            }
        } catch (Exception e) {
            Utils.criarArquivoUltimoID(bkpId);
            Selenide.closeWindow();
            test.log(Status.ERROR, "Ocorreu um erro durante a execução do fluxo, erro: " + e.getMessage());
            extent.flush();
            throw new Exception(e.getMessage());
        }
    }
    private void processarNovaInsercao(JsonPath jsonPath) throws Exception {
        String customCode = jsonPath.getString("items[0].custom_code");
        String quantity = jsonPath.getString("items[0].quantity");
        String id = jsonPath.getString("items[0].id");
        itens.novaInsercao(customCode, quantity);

        adicionarItemInserido(id);
        Utils.criarArquivo(null, id, null);
        Utils.criarArquivoUltimoID(id);
        criarArquivoYAML();
    }

    private void adicionarItemInserido(String id) {
        ItensInseridosDto item = new ItensInseridosDto(id);
        itensInseridos.add(item);
    }

    protected void criarArquivoYAML() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.writeValue(new File("src/test/resources/dataMass/itens_inseridos.yaml"), itensInseridos);
    }

    @AfterMethod
    public void after() {
        closeWindow();
        closeWebDriver();
    }
}
