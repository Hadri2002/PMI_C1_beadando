import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private Patient patient;
    private LocalDate date;
    private LocalTime time;
    private Duration duration;
    private LocalTime endTime;


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Appointment() {
         this.endTime = this.time.plusMinutes(this.duration.toMinutes());
    }

    public Appointment(Patient patient, LocalDate date, LocalTime time, Duration duration) {
        this.patient = patient;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.endTime = this.time.plusMinutes(this.duration.toMinutes());
    }

    @Override
    public String toString() {
        return "Name: " + this.getPatient().getName() + "\r\nBirtday: " +
                this.getPatient().getBirthday() + "\r\nAppointment: " +
                this.getTime() + " - " + this.getEndTime();
    }
}
