package constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GenericConstants {
    public static final String START_DATE = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    public static final String END_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
}
