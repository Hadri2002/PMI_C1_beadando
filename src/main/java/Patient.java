import java.time.LocalDate;

public class Patient {
    private String name;
    private String taj;

    //private LocalDate birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaj() {
        return taj;
    }

    public void setTaj(String taj) {
        this.taj = taj;
    }

    public Patient() {
    }

    public Patient(String name, String taj) {
        this.name = name;
        this.taj = taj;
    }
}
