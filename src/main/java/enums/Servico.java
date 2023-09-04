package enums;

public enum Servico {
    UPA_CUMBICA("upa_cumbica");

    private final String path;

    Servico(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}