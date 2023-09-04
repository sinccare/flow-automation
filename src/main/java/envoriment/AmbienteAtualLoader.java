package envoriment;

import enums.Ambiente;
import org.apache.commons.lang3.StringUtils;

import static enums.Ambiente.DEV;
import static enums.Ambiente.HOM;

public class AmbienteAtualLoader {

    public static Ambiente get() {
        String env = System.getProperty("env");
        if (StringUtils.isEmpty(env)) {
            System.setProperty("env", "hom");
            return HOM;
        }
        switch (env) {
            case "dev":
                return DEV;
            case "hom":
                return HOM;
            default:
                throw new IllegalArgumentException("Ambiente '" + env + "' não configurado para execução dos testes");
        }
    }
}