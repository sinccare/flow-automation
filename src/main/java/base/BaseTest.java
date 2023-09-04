package base;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dto.ItensInseridosDto;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pageobject.*;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
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

    protected void consultarDados() throws Exception {
        String ultimoId = Utils.lerArquivo("ultimoItemInserido", "last_id");
        JsonPath jsonPath = Utils.consultarMassaDeDados(START_DATE, END_DATE, ultimoId);

        try {
            if (!jsonPathIsNull(jsonPath)) {
                processarDadosConsulta(jsonPath, ultimoId);
            } else {
                Utils.criarArquivoUltimoID(ultimoId);
                Utils.criarArquivo(null, ultimoId, null);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void processarDadosConsulta(JsonPath jsonPath, String ultimoId) throws Exception {
        Utils.criarArquivo(
                jsonPath.getString("items[0].custom_code"),
                jsonPath.getString("items[0].id"),
                jsonPath.getString("items[0].quantity"));

        abrirNavegadorELogar();
        Utils.criarArquivoUltimoID(ultimoId);
    }

    private void abrirNavegadorELogar() {
        open("https://guarulhoshospitalar.sissonline.com.br/Abertura/Login.aspx");
        login.logar();
        home.acessarModuloSuprimentos();
        suprimentos.acessarPrescricaoDireta();
        prescricaoDireta.selecionarPaciente("PACIENTE TESTE");
    }

    protected void inserirSuprimentos() throws Exception {
        String bkpId = null;
        try {
            String lastCustomCode = Utils.lerArquivo("massaDeDados", "last_custom_code");
            String lastQuantity = Utils.lerArquivo("massaDeDados", "last_quantity");
            bkpId = Utils.lerArquivo("massaDeDados", "last_id");

            if (lastCustomCode != null) {
                processarInsercaoSuprimentos(lastCustomCode, lastQuantity, bkpId);

            } else {
                Utils.criarArquivo(null, Utils.lerArquivo("massaDeDados", "last_id"), null);
                Selenide.closeWindow();
                throw new Exception("A consulta não retornou dados a serem inseridos!");
            }
        } catch (Exception e) {
            Utils.criarArquivoUltimoID(bkpId);
            throw new Exception(e.getMessage());
        }
    }

    private void processarInsercaoSuprimentos(String lastCustomCode, String lastQuantity, String bkpId) throws Exception {
        itens.pesquisarItens("ALTEPLASE");
        itens.selecionarItens(lastCustomCode);
        itens.inserirItens(lastQuantity);

        adicionarItemInserido(bkpId);
        Utils.criarArquivoUltimoID(bkpId);
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
                    break;
                } else {
                    processarNovaInsercao(jsonPath);
                }
            }
        } catch (Exception e) {
            Utils.criarArquivoUltimoID(bkpId);
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

