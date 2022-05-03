import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class test {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        /*LocalDate date = LocalDate.parse("2022-12-31");
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        String dateString = formatter.format(date);
        System.out.println(dateString);*/
        /*
        String date = scan.nextLine();

        LocalDate dateDate = LocalDate.parse(date);
        System.out.println(dateDate);*/

        /*LocalTime start = LocalTime.parse("10:00");
        int durationMinutes = scanner.nextInt();
        Duration duration = Duration.ofMinutes(durationMinutes);
        System.out.println(duration.toString());

        System.out.println(start);
        LocalTime end = start.plusMinutes(duration.toMinutes());
        System.out.println(end);

        System.out.println(start.compareTo(start));*/


        /*ArrayList<Appointment> appointments = Methods.readFromXml("src/main/resources/appointments.xml");

        for (int i = 0; i < appointments.size(); i++){
            System.out.println(appointments.get(i).getPatient().getName());
            System.out.println(appointments.get(i).getPatient().getBirthday());
            System.out.println(appointments.get(i).getDate());
            System.out.println(appointments.get(i).getDuration());
            System.out.println(appointments.get(i).getTime());
        }

        Methods.writeToXml(appointments, "src/main/resources/appointments.xml");

        System.out.println(appointments.get(1).getEndTime());*/

        int a = Integer.parseInt("01b"); //NumberFormatException
        System.out.println(a);

    }
}

    /*public static void deleteAppointment(ArrayList<Appointment> appointments, String filepath) {
        System.out.println("You are now selecting which appointment to delete.");
        listAppointments(appointments);

        System.out.println("Enter the name of the patient you'd like to delete:");
        String name = enterName();

        boolean nameFound = false;
        String nameFoundString = "The following patient birthdays were found under this name:\r\n";

        for(Appointment appointment: appointments) {
            if(name.equals(appointment.getPatient().getName())) {
                nameFound = true;
                nameFoundString += appointment.getPatient().getBirthday() + "\r\n";
            }
        }

        if (nameFound != true) {
            System.err.println("There are no appointments recorded under that patient name.");
            return;
        }
        else {
            System.out.print(nameFoundString);

            System.out.println("Enter the birthday of the chosen patient in the following format: YYYY-MM-DD");
            LocalDate birthday = enterDate();

            boolean deletable = false;
            for(Appointment appointment: appointments) {
                if(appointment.getPatient().getName().equals(name) && appointment.getPatient().getBirthday().equals(birthday)) {
                    appointments.remove(appointment);
                    writeToXml(appointments, filepath);
                    deletable = true;
                    break;
                }
            }

            if(deletable == false) {
                System.err.println("The deletion was unsuccessful, due to a wrongly entered birthday!");
            }
            else {
                System.out.println("Patient successfully deleted");
            }
        }
    }*/
