package property;

import enums.Ambiente;
import enums.Servico;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class PropertyLoader {

    public static PropertyLoader get() {
        return new PropertyLoader();
    }

    public String getUrl(Ambiente ambiente, Servico servico) {

        Yaml yaml = new Yaml();
        Map<String, Object> root = yaml.load(getClass().getClassLoader().getResourceAsStream(ambiente.getFilePath()));

        Map<String, Object> services = (Map<String, Object>) root.get("services");
        Map<String, String> service = (Map<String, String>) services.get(servico.getPath());
        return service.get("url");
    }
}