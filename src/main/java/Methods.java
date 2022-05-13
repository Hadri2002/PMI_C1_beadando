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
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
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
                createChildElement(document, patientElement, "taj", appointment.getPatient().getTaj());
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

                    String name = "", taj = "", date = "1900-10-22", time = "00:00", duration = "0";
                    for (int j = 0; j < childNodesOfAppointmentTag.getLength(); j++) {

                        if (childNodesOfAppointmentTag.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            if(childNodesOfAppointmentTag.item(j).getNodeName().equals("patient")) {
                                NodeList patientNodes = childNodesOfAppointmentTag.item(j).getChildNodes();
                                for(int h = 0; h < patientNodes.getLength(); h++) {
                                    if(patientNodes.item(h).getNodeType() == Node.ELEMENT_NODE) {
                                        switch(patientNodes.item(h).getNodeName()) {
                                            case "name": name = patientNodes.item(h).getTextContent(); break;
                                            case "taj": taj = patientNodes.item(h).getTextContent(); break;
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
                        Patient patient = new Patient(name, taj);
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
        System.out.println(ANSI_YELLOW + "The following appointments have been recorded thus far:\r\n" + ANSI_RESET);
        for(Appointment appointment: appointments) {
            System.out.println(appointment);
            System.out.println();
        }
    }

    public static void addNewAppointment (ArrayList<Appointment> appointments, String filepath) {
        System.out.println(ANSI_YELLOW + "\r\nYou are now adding a new patient into the database! Please note the " +
                "following rules about appointments:\r\n- The doctor takes patients all days of the week from 08:00 to " +
                "16:00\r\n- A patient that already has an appointment can not have another one recorded in the system " +
                "(this is based on TAJ numbers alone)\r\n- You can only enter appointment dates for tomorrow\r\n" + ANSI_RESET);

        boolean patientCheck = false;
        Patient patient = new Patient();
        while(!patientCheck) {
            System.out.println("Enter the name of the new patient: ");
            String name = enterName();

            boolean nameCheck = false;
            StringBuilder nameCheckString = new StringBuilder("\r\nThe name entered has been found in the system with the following TAJ number " +
                    "(if the new patient has a different TAJ number, this shouldn't pose an issue):\r\n");
            ArrayList<String> tajOfCheckedAppointment = new ArrayList<>();
            for(Appointment appointment: appointments) {
                if(appointment.getPatient().getName().equals(name) && tajOfCheckedAppointment.contains(appointment.getPatient().getTaj()) == false) {
                    tajOfCheckedAppointment.add(appointment.getPatient().getTaj());
                    nameCheck = true;
                }
            }

            if(nameCheck) {
                for (String taj : tajOfCheckedAppointment) {
                    nameCheckString.append(taj).append("\r\n");
                }
                System.out.println(ANSI_YELLOW + nameCheckString + ANSI_RESET);
            }

            System.out.println("Enter the TAJ number of the patient: ");
            String taj = enterTaj();
            patient = new Patient(name, taj);

            for(Appointment appointment: appointments) {
                if(appointment.getPatient().getTaj().equals(taj) && !appointment.getPatient().getName().equals(name)) { //taj létezik, másik név alatt
                    System.err.println("The following patient has been recorded under this TAJ number: " + appointment.getPatient().getName() + "\r\n");
                    patientCheck = false;
                    break;
                }
                else if(appointment.getPatient().getTaj().equals(taj) && appointment.getPatient().getName().equals(name)) { //taj létezik, ez alatt a név alatt
                    System.err.println("\r\nThis patient already has an appointment! One patient can not have an " +
                            "appointment twice! Please either delete the previous appointment or modify its details instead!\r\n");
                    return;
                }
                else {
                    patientCheck = true;
                }
            }
        }


        boolean dateCheck = false;
        LocalDate appointmentDate = LocalDate.parse("1000-01-01");
        LocalTime appointmentTime = LocalTime.parse("00:00");
        Duration duration = Duration.ofMinutes(0);

        while(!dateCheck) {
            System.out.println("Please enter the date of the appointment in the following format: YYYY-MM-DD");

            appointmentDate = enterDate(appointments);

            System.out.println("Please enter the time of the appointment in the following format: HH:MM");

            appointmentTime = enterTime();

            System.out.println("Please enter the expected duration of the check-up in minutes: ");

            duration = enterDuration();

            for(Appointment appointment: appointments) {
                if(appointment.getDate().equals(appointmentDate)) {
                    LocalTime minTime, maxTime;
                    if(appointment.getTime().isAfter(appointmentTime)) maxTime = appointment.getTime();
                    else maxTime = appointmentTime;
                    if(appointment.getEndTime().isBefore(appointmentTime.plusMinutes(duration.toMinutes()))) minTime = appointment.getEndTime();
                    else minTime = appointmentTime.plusMinutes(duration.toMinutes());
                    if(minTime.isAfter(maxTime)) {

                        System.err.println("\r\nThere's already an appointment at the given date and time with the following details: ");
                        System.out.println(appointment + "\r\n");
                        dateCheck = false;
                        break;
                    }
                }

                if(appointmentTime.plusMinutes(duration.toMinutes()).isBefore(LocalTime.parse("16:00"))) {
                    dateCheck = true;
                }
                else {
                    System.err.println("\r\nThe appointment does not end before 16:00!\r\n");
                    dateCheck = false;
                    break;
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

    private static LocalDate enterDate(ArrayList<Appointment> appointments) {
        LocalDate date = LocalDate.parse("1900-01-01");
        while(date.isEqual(LocalDate.parse("1900-01-01"))){
            try{
                String dateString = scanner.nextLine();
                date = LocalDate.parse(dateString);
                if(!date.isAfter(LocalDate.now())) {
                    date = LocalDate.parse("1900-01-01");
                    System.err.println("You can only enter appointments from the next day on!");
                }
            }
            catch(DateTimeParseException e) {
                System.err.println("Wrong format! Please write the date in the following format: YYYY-MM-DD");
            }
        }

        boolean dateTaken = false;
        StringBuilder dateTakenString = new StringBuilder("\r\nPlease note that the following appointments already exist at the given date:\r\n\r\n");
        for(Appointment appointment: appointments) {
            if(appointment.getDate().equals(date)) {
                dateTakenString.append(appointment);
                dateTakenString.append("\r\n\r\n");
                dateTaken = true;
            }
        }

        if(!dateTaken) {
            System.out.println(ANSI_YELLOW + "There are no appointments on the given date yet!" + ANSI_RESET);
        }
        else {
            System.out.println(ANSI_YELLOW + dateTakenString + ANSI_RESET);
        }
        return date;
    }

    private static LocalTime enterTime() {
        LocalTime time = LocalTime.parse("00:00");
        while(time.compareTo(LocalTime.parse("00:00")) == 0){
            try{
                String timeString = scanner.nextLine();
                time = LocalTime.parse(timeString);
                if(time.isBefore(LocalTime.parse("08:00"))) {
                    System.err.println("Please note that the doctor does not begin examinations before 8:00!");
                    time = LocalTime.parse("00:00");
                }
                else if(time.isAfter(LocalTime.parse("16:00"))) {
                    System.err.println("Please note that the doctor does not begin examinations after 16:00!");
                    time = LocalTime.parse("00:00");
                    System.out.println("Enter a new time: ");
                }
            }
            catch(DateTimeParseException e) {
                System.err.println("Wrong format! Please write the time in the following format: HH:MM");
            }
        }
        return time;
    }

    private static Duration enterDuration() {
        int durationInMinutes = 0;
        while(durationInMinutes <= 0) {
            try {
                durationInMinutes = scanner.nextInt();
                scanner.nextLine();
                if (durationInMinutes <= 0) {
                    System.err.println("Invalid option");
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid option");
                scanner.nextLine();
            }
        }
        return Duration.ofMinutes(durationInMinutes);
    }

    private static String enterTaj() {
        String taj = "";
        while(taj.isEmpty()) {
            taj = scanner.nextLine();
            try {
                Integer.parseInt(taj);
                if (taj.length() != 9) {
                    System.err.println("The TAJ number has 9 digits! Enter the correct TAJ number: ");
                    taj = "";
                }
            } catch (NumberFormatException e) {
                taj = "";
                System.err.println("The TAJ number can only have 9 numbers in it! Enter the correct TAJ number: ");
            }
        }
        return taj;
    }

    public static void deleteAppointment(ArrayList<Appointment> appointments, String filepath) {
        System.out.println(ANSI_YELLOW + "You are now selecting which appointment to delete." + ANSI_RESET);
        listAppointments(appointments);
        System.out.println("Enter the TAJ number of the chosen patient to select and confirm your deletion: ");
        String taj = enterTaj();

        boolean deletable = false;
        for(Appointment appointment: appointments) {
            if(appointment.getPatient().getTaj().equals(taj)) {
                appointments.remove(appointment);
                writeToXml(appointments, filepath);
                deletable = true;
                break;
            }
        }

        if(!deletable) {
            System.err.println("\r\nThe deletion was unsuccessful, due to a wrongly entered TAJ number!\r\n");
        }
        else {
            System.out.println("\r\nPatient successfully deleted\r\n");
        }
    }

    public static void modifyAppointment(ArrayList<Appointment> appointments, String filepath) {
        System.out.println(ANSI_YELLOW + "\r\nYou are now adding modifying an existing patient in the database! " +
                "Please note the following rules about appointments:\r\n- The doctor takes patients all days of the " +
                "week from 08:00 to 16:00\r\n- A patient that already has an appointment can not have another one" +
                " recorded in the system\r\n" + ANSI_RESET);
        listAppointments(appointments);

        System.out.println("Enter the TAJ number of the patient whose appointment you'd like to modify:");
        String taj = enterTaj(), name = "";
        LocalDate date = LocalDate.parse("1900-01-01");
        LocalTime time = LocalTime.parse("00:00");
        Duration duration = Duration.ofMinutes(0);

        boolean tajFound = false;
        for(Appointment appointment: appointments) {
            if(appointment.getPatient().getTaj().equals(taj)) {
                name = appointment.getPatient().getName();
                date = appointment.getDate();
                time = appointment.getTime();
                duration = appointment.getDuration();
                appointments.remove(appointment);
                tajFound = true;
                break;
            }
        }

        if(!tajFound) {
            System.err.println("\r\nNo patients were found under that TAJ number!\r\n");
            return;
        }

        Patient modifiedPatient = new Patient(name, taj);
        Appointment appointmentToModify = new Appointment(modifiedPatient, date, time, duration);

        int menu = -1;
        System.out.println("Please enter one of the following numbers to choose which attribution you'd like to change: ");
        String options = "\r\n1 - Name\r\n2 - TAJ number\r\n3 - Date, time and duration of appointment\r\n4 - Stop modification\r\n";

        while(menu != 4) {
            System.out.println(options);
            try{
                menu = scanner.nextInt();
                scanner.nextLine();
                if(menu < 1 || menu > 4) {
                    System.err.println("Invalid option!");
                }
                else {
                    switch(menu) {
                        case 1: modifyName(appointmentToModify);
                        System.out.println("\r\nName successfully changed!\r\n"); break; //name
                        case 2: modifyTaj(appointmentToModify, appointments);
                            System.out.println("\r\nTAJ successfully changed\r\n"); break; //taj
                        case 3: modifyDateAndTime(appointmentToModify, appointments);
                        System.out.println("\r\nDate, time and duration successfully changed!\r\n"); break; //date-time-duration
                        case 4: break; //cancel
                    }
                }
            }
            catch(InputMismatchException e) {
                System.err.println("Invalid option!");
                scanner.nextLine();
            }
        }
        appointments.add(appointmentToModify);
        writeToXml(appointments, filepath);
    }

    private static void modifyName (Appointment appointmentToModify) {
        System.out.println("Please enter the new name: ");
        String name = enterName();
        Patient patient = new Patient(name, appointmentToModify.getPatient().getTaj());
        appointmentToModify.setPatient(patient);
    }

    private static void modifyTaj (Appointment appointmentToModify, ArrayList<Appointment> appointments) {
        String taj = "";

        while(taj.isEmpty()) {
            System.out.println("Please enter the new TAJ ");
            taj = enterTaj();

            for(Appointment appointment: appointments) {
                if(appointment.getPatient().getTaj().equals(taj)) {
                    System.err.println("There is already a user under that TAJ number!");
                    taj = "";
                }
            }
        }

        Patient patient = new Patient(appointmentToModify.getPatient().getName(), taj);
        appointmentToModify.setPatient(patient);
    }

    private static void modifyDateAndTime (Appointment appointmentToModify, ArrayList<Appointment> appointments) {
        boolean dateCheck = false;
        LocalDate appointmentDate = LocalDate.parse("1000-01-01");
        LocalTime appointmentTime = LocalTime.parse("00:00");
        Duration duration = Duration.ofMinutes(0);

        while(dateCheck == false) {
            System.out.println("Please enter the date of the appointment in the following format: YYYY-MM-DD");

            appointmentDate = enterDate(appointments);

            System.out.println("Please enter the time of the appointment in the following format: HH:MM");

            appointmentTime = enterTime();

            System.out.println("Please enter the expected duration of the check-up in minutes: ");

            duration = enterDuration();

            for(Appointment appointment: appointments) {
                if(appointment.getDate().equals(appointmentDate)) {
                    LocalTime minTime, maxTime;
                    if(appointment.getTime().isAfter(appointmentTime)) maxTime = appointment.getTime();
                    else maxTime = appointmentTime;
                    if(appointment.getEndTime().isBefore(appointmentTime.plusMinutes(duration.toMinutes()))) minTime = appointment.getEndTime();
                    else minTime = appointmentTime.plusMinutes(duration.toMinutes());
                    if(minTime.isAfter(maxTime)) {

                        System.err.println("\r\nThere's already an appointment at the given date and time with the following details: ");
                        System.out.println(appointment + "\r\n");
                        dateCheck = false;
                        break;
                    }
                }

                if(appointmentTime.plusMinutes(duration.toMinutes()).isBefore(LocalTime.parse("16:00"))) {
                    dateCheck = true;
                }
                else {
                    System.err.println("\r\nThe appointment does not end before 16:00!\r\n");
                    dateCheck = false;
                    break;
                }
            }
        }
        appointmentToModify.setDate(appointmentDate);
        appointmentToModify.setTime(appointmentTime);
        appointmentToModify.setDuration(duration);
    }

}