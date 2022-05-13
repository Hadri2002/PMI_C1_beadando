import java.time.LocalDate;

public class Patient {
    private String name;
    private String taj;

    public String getName() {
        return name;
    }

    public String getTaj() {
        return taj;
    }

    public Patient() {
    }

    public Patient(String name, String taj) {
        this.name = name;
        this.taj = taj;
    }
}
