import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Methods {
    private static final Scanner scanner = new Scanner(System.in);

    public static void writeToXml(ArrayList<Appointment> appointments, String filepath) {
        try{
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("appointments");
            document.appendChild(rootElement);


            for(Appointment appointment : appointments) {
                Element appointmentElement = document.createElement("appointment");
                rootElement.appendChild(appointmentElement);
                Element patientElement = document.createElement("patient");
                appointmentElement.appendChild(patientElement);

                createChildElement(document, patientElement, "name", appointment.getPatient().getName());
                createChildElement(document, patientElement, "birthday", appointment.getPatient().getBirthday().toString());
                createChildElement(document, appointmentElement, "date", appointment.getDate().toString());
                createChildElement(document, appointmentElement, "time", appointment.getTime().toString());
                createChildElement(document, appointmentElement, "duration", String.valueOf(appointment.getDuration().toMinutes()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(filepath));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void createChildElement(Document document, Element parent, String tagName, String value) {
        Element element = document.createElement(tagName);
        element.setTextContent(value);
        parent.appendChild(element);
    }

    public static ArrayList<Appointment> readFromXml(String filepath) {
        ArrayList<Appointment> appointments = new ArrayList<>();

        try{
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filepath);

            Element rootElement = document.getDocumentElement();
            NodeList childNodesList = rootElement.getChildNodes();
            Node node;
            for (int i = 0; i < childNodesList.getLength(); i++) {
                node = childNodesList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfAppointmentTag = node.getChildNodes();

                    String name = "", birthday = "", date = "1900-10-22", time = "00:00", duration = "0";
                    for (int j = 0; j < childNodesOfAppointmentTag.getLength(); j++) {


                        if (childNodesOfAppointmentTag.item(j).getNodeType() == Node.ELEMENT_NODE) {

                            if(childNodesOfAppointmentTag.item(j).getNodeName().equals("patient")) {
                                NodeList patientNodes = childNodesOfAppointmentTag.item(j).getChildNodes();
                                for(int h = 0; h < patientNodes.getLength(); h++) {

                                    if(patientNodes.item(h).getNodeType() == Node.ELEMENT_NODE) {

                                        switch(patientNodes.item(h).getNodeName()) {
                                            case "name": name = patientNodes.item(h).getTextContent(); break;
                                            case "birthday": birthday = patientNodes.item(h).getTextContent(); break;
                                        }
                                    }
                                }
                            }
                            else{
                                switch (childNodesOfAppointmentTag.item(j).getNodeName()) {
                                    case "date": date = childNodesOfAppointmentTag.item(j).getTextContent(); break;
                                    case "time": time = childNodesOfAppointmentTag.item(j).getTextContent(); break;
                                    case "duration": duration = childNodesOfAppointmentTag.item(j).getTextContent(); break;
                                }
                            }

                        }
                    }

                    try{
                        Patient patient = new Patient(name, LocalDate.parse(birthday));
                        Appointment appointment = new Appointment(patient, LocalDate.parse(date), LocalTime.parse(time), Duration.ofMinutes(Integer.parseInt(duration)));
                        appointments.add(appointment);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        return appointments;
    }

    public static void listAppointments(ArrayList<Appointment> appointments) {
        System.out.println("The following appointments have been recorded thus far:\r\n");
        for(Appointment appointment: appointments) {
            System.out.println(appointment);
            System.out.println();
        }

    }

    public static void addNewAppointment(ArrayList<Appointment> appointments, String filepath) {

        System.out.println("Enter the name of the new patient: ");
        String name = enterName();

        System.out.println("Enter the birthday of the patient in the following format: YYYY-MM-DD");
        LocalDate birthday = enterDate();

        for(Appointment appointment: appointments) {
            if(name.equals(appointment.getPatient().getName()) && birthday.equals(appointment.getPatient().getBirthday())) {
                System.err.println("This patient already has an appointment!");
                return;
            }
        }

        Patient patient = new Patient(name, birthday);

        boolean dateCheck = false;
        LocalDate appointmentDate = LocalDate.parse("1000-01-01");
        LocalTime appointmentTime = LocalTime.parse("00:00");
        Duration duration = Duration.ofMinutes(0);

        while(dateCheck == false) {
            System.out.println("Please enter the date of the appointment in the following format: YYYY-MM-DD");

            appointmentDate = enterDate();

            boolean dateTaken = false;
            String dateTakenString = "\r\nPlease note that the following appointments already exist at the given date:\r\n\r\n";

            for(Appointment appointment: appointments) {
                if(appointment.getDate().equals(appointmentDate)) {
                    dateTakenString += appointment.toString();
                    dateTakenString += "\r\n\r\n";
                    dateTaken = true;
                }
            }

            if(dateTaken == false) {
                System.out.println("There are no appointments on the given date and time!");
            }
            else {
                System.out.println(dateTakenString);
            }

            System.out.println("Please enter the time of the appointment in the following format: HH:MM");

            appointmentTime = enterTime();

            System.out.println("Please enter the expected duration of the check-up in minutes: ");

            int durationInMinutes = 0;

            while(durationInMinutes <= 0){
                try{
                    durationInMinutes = scanner.nextInt();
                    scanner.nextLine();
                    if(durationInMinutes <= 0) {
                        System.err.println("Invalid option");
                    }
                }
                catch(InputMismatchException e){
                    System.err.println("Invalid option");
                    scanner.nextLine();
                }
            }

            duration = Duration.ofMinutes(durationInMinutes);

            for(Appointment appointment: appointments) {
                if(appointment.getDate().equals(appointmentDate)) {
                    if((appointmentTime.isAfter(appointment.getTime()) && appointmentTime.isBefore(appointment.getEndTime())) ||
                            (appointmentTime.plusMinutes(duration.toMinutes()).isAfter(appointment.getTime()) &&
                                    appointmentTime.plusMinutes(duration.toMinutes()).isBefore(appointment.getEndTime()))) {


                        System.err.println("There's already an appointment at the given date with the following details: ");
                        System.out.println(appointment);
                        dateCheck = false;
                        break;
                    }
                    else {
                        dateCheck = true;
                    }
                }
                else {
                    dateCheck = true;
                }
            }
        }


        appointments.add(new Appointment(patient, appointmentDate, appointmentTime, duration));

        writeToXml(appointments, filepath);

    }

    private static String enterName() {
        String name = "";

        while(name.isEmpty()) {
            name = scanner.nextLine();
            if(name.isEmpty()) {
                System.err.println("You did not enter a name");
            }
        }

        return name;
    }

    private static LocalDate enterDate() {
        LocalDate date = LocalDate.parse("1900-01-01");

        while(date.isEqual(LocalDate.parse("1900-01-01"))){
            try{
                String dateString = scanner.nextLine();
                date = LocalDate.parse(dateString);
            }
            catch(DateTimeParseException e) {
                System.err.println("Wrong format! Please write the date in the following format: YYYY-MM-DD");
            }
        }

        return date;
    }

    private static LocalTime enterTime() {
        LocalTime time = LocalTime.parse("00:00");

        while(time.compareTo(LocalTime.parse("00:00")) == 0){
            try{
                String timeString = scanner.nextLine();
                time = LocalTime.parse(timeString);
            }
            catch(DateTimeParseException e) {
                System.err.println("Wrong format! Please write the time in the following format: HH:MM");
            }
        }


        return time;
    }

    public static void deleteAppointment(ArrayList<Appointment> appointments, String filepath) {
        System.out.println("You are now selecting which appointment to delete.");
        listAppointments(appointments);

        System.out.println("Enter the name of the patient you'd like to delete:");
        String name = enterName();

        boolean nameFound = choosePatient(appointments, name);

        if (nameFound != true) {
            return;
        }
        else {
            System.out.println("Enter the birthday of the chosen patient in the following format to select and " +
                    "confirm your deletion: YYYY-MM-DD");
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
                System.out.println("Patient successfully deleted\r\n");
            }
        }
    }

    private static boolean choosePatient(ArrayList<Appointment> appointments, String name) {
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
        }
        else {
            System.out.print(nameFoundString);
        }

        return nameFound;
    }

    public static void modifyAppointment(ArrayList<Appointment> appointments, String filepath) {
        System.out.println("You are now selecting which appointment to modify.");
        listAppointments(appointments);

        System.out.println("Enter the name of the patient whose appointment you'd like to modify:");
        String name = enterName();


        writeToXml(appointments, filepath);
    }

}