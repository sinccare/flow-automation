package utils;

import base.BaseRequest;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;



public class Utils {
    static BaseRequest baseRequest = new BaseRequest();

    public static JsonPath consultarMassaDeDados(String startDate, String endDate, String last_code){
        JsonPath jsonPath = baseRequest.getInventory("RPA_UPAMARIADIRCE", "RPA@robo@2020",
                        startDate, endDate, last_code)
                .statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath();
        return jsonPath;
    }

    public static String lerArquivo(String nomeArquivo, String nomeVariavel) {
        Yaml yaml = new Yaml();
        Map<String, Object> configYaml = null;
        String arquivo = nomeArquivo;

        try {
            configYaml = yaml.load(new FileInputStream(
                    "./src/test/resources/dataMass/" + arquivo + ".yaml"));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> configPath = Arrays.asList(nomeVariavel.split("\\."));
        Object config = configYaml.get(configPath.get(0));
        for (int i = 1; i < configPath.size(); i++) {
            config = ((Map<String, Object>) config).get(configPath.get(i));
        }
        if (Objects.isNull(config)) config = "";
        return String.valueOf(config);
    }

    public static void criarArquivo(String custom_code, String lastId, String qtdd) throws IOException {
        Map<String, Object> processo = new HashMap<String, Object>();
        processo.put("last_custom_code", custom_code);
        processo.put("last_id", lastId);
        processo.put("last_quantity", qtdd);

        Yaml yaml = new Yaml();
        FileWriter writer = new FileWriter("src/test/resources/dataMass/massaDeDados.yaml");
        yaml.dump(processo, writer);
    }
    public static void criarArquivoUltimoID(String ultimo_id) throws IOException {
        Map<String, Object> processo = new HashMap<String, Object>();
        processo.put("last_id", ultimo_id);

        Yaml yaml = new Yaml();
        FileWriter writer = new FileWriter("src/test/resources/dataMass/ultimoItemInserido.yaml");
        yaml.dump(processo, writer);
    }


}
