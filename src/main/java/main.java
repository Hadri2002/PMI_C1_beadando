import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String filepath= "src/main/resources/appointments.xml";

    public static void main(String[] args) {
        ArrayList<Appointment> appointments = Methods.readFromXml(filepath);

        System.out.println();
        System.out.println("Welcome to Dr. Louis Creed's general practitioner clinic!");
        System.out.println();

        System.out.println("Please choose one of the options from the menu below by typing the correct number!");
        int menu = -1;

        while(menu != 5) {
            switch(menu) {
                case 1: Methods.addNewAppointment(appointments, filepath); break;
                case 2: Methods.modifyAppointment(appointments, filepath); break;
                case 3: Methods.deleteAppointment(appointments, filepath); break;
                case 4: Methods.listAppointments(appointments); break;
            }

            System.out.println("1 - Record new appointment\r\n2 - Modify appointment\r\n3 - Delete existing apointment" +
                    "\r\n4 - List appointments\r\n5 - Exit programme");

            try{
                menu = scanner.nextInt();
                scanner.nextLine();

                if(menu < 1 || menu > 5) {
                    System.err.println("\r\nNot valid option\r\n");
                }
            }
            catch(InputMismatchException e){
                System.err.println("\r\nNot valid option\r\n");
                scanner.nextLine();
            }

        }
    }
}