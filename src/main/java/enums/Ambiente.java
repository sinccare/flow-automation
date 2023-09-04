package enums;

public enum Ambiente {

    DEV("configuration-dev.yml"),
    HOM("configuration-hom.yml");

    private final String filePath;

    Ambiente(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}