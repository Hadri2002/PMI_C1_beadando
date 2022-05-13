import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String filepath= "src/main/resources/appointments.xml";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        ArrayList<Appointment> appointments = Methods.readFromXml(filepath);

        System.out.println();
        System.out.println(ANSI_YELLOW + "Welcome to Dr. Louis Creed's clinic!" + ANSI_RESET);
        System.out.println();
        System.out.println("Please choose one of the options from the menu below by typing the correct number!");

        int menu = -1;
        while(menu != 5) {
            System.out.println("1 - Record new appointment\r\n2 - Modify appointment\r\n3 - Delete existing apointment" +
                    "\r\n4 - List appointments\r\n5 - Exit programme");

            try{
                menu = scanner.nextInt();
                scanner.nextLine();

                if(menu < 1 || menu > 5) {
                    System.err.println("\r\nNot a valid option!\r\n");
                }
                else {
                    switch(menu) {
                        case 1: Methods.addNewAppointment(appointments, filepath); break;
                        case 2: Methods.modifyAppointment(appointments, filepath); break;
                        case 3: Methods.deleteAppointment(appointments, filepath); break;
                        case 4: Methods.listAppointments(appointments); break;
                    }
                }
            }
            catch(InputMismatchException e){
                System.err.println("\r\nYou did not enter a number!\r\n");
                scanner.nextLine();
            }

        }
    }
}