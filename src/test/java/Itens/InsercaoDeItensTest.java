package Itens;

import base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.Utils;

public class InsercaoDeItensTest extends BaseTest {
    private ExtentReports extent;
    private ExtentTest test;

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

    @Test
    public void fluxoInsercaoSuprimentos() {
        try {
            try {
                consultarDados();
            } catch (NoSuchElementException e) {
                handleException("Elemento não encontrado: " + e.getMessage());
            } catch (NullPointerException e) {
                handleException("Não existe itens a serem inseridos nesse momento!" +
                test.log(Status.INFO, "O último id utilizado na consulta foi: " + Utils.lerArquivo("ultimoItemInserido", "last_id")));
            } catch (Exception e) {
                handleException("Ocorreu uma exceção: " + e.getMessage());
            }

            try {
                inserirSuprimentos();
                criarArquivoYAML();
                test.log(Status.PASS, "Os itens inseridos foram:");
                test.log(Status.INFO, Utils.lerArquivo("itens_inseridos", "id"));
                consultarDados();
            } catch (NoSuchElementException e) {
                handleException("Elemento não encontrado: " + e.getMessage());
            } catch (Exception e) {
                handleException("O último id utilizado na consulta foi: "
                        + Utils.lerArquivo("ultimoItemInserido", "last_id"));
            }

            try {
                novaInsercao();
                criarArquivoYAML();
                test.log(Status.PASS, "Os itens inseridos foram:");
                test.log(Status.INFO, Utils.lerArquivo("itens_inseridos", "id"));
            } catch (NoSuchElementException e) {
                handleException( "Elemento não encontrado: " + e.getMessage());
            } catch (Exception e) {
                handleException( "Não existe itens a serem inseridos nesse momento!");
            }
        } catch (NullPointerException e) {
            handleException( "Ocorreu uma exceção: " + e.getMessage());
        }
    }
    private void handleException(String message) {
        test.log(Status.FAIL, "Não existe itens a serem inseridos nesse momento!");
        test.log(Status.INFO, message);
        extent.flush();
        Assert.fail();
    }

    @AfterMethod
    public void tearDown(){
        extent.flush();
    }
}
