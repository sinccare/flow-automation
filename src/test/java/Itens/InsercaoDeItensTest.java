package Itens;

import base.BaseTest;
import org.testng.annotations.Test;


public class InsercaoDeItensTest extends BaseTest {

    @Test
    public void fluxoInsercaoSuprimentos() throws Exception {
        consultarDados();
        inserirSuprimentos();
        consultarDados();
        novaInsercao();
        criarArquivoYAML();
    }
}
