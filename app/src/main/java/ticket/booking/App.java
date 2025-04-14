package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) {

        System.out.println("Running Train booking system.");
        Scanner sc = new Scanner(System.in);

        int option = 0;

        UserBookingService userBookingService;

        // In App.java main method
        try {
            // Use the constructor that doesn't require logged-in user initially
            userBookingService = new UserBookingService();
        } catch (IOException e) {
            // Use System.err for errors
            System.err.println("FATAL ERROR: Could not initialize User Booking Service. Check file paths, permissions, and JSON content.");
            System.err.println("Error details: " + e.getMessage()); // Print the specific error
            e.printStackTrace(); // Print the full stack trace for debugging
            return; // Exit the application
        }

        while (option != 7) {
            System.out.println("Choose from options.");
            System.out.println("1. Signup.");
            System.out.println("2. Login.");
            System.out.println("3. Fetch bookings.");
            System.out.println("4. Search trains.");
            System.out.println("5. Book a seat.");
            System.out.println("6. Cancel my booking.");
            System.out.println("7. Exit the app.");
            option = sc.nextInt();

            Train trainSelectedForBooking = new Train();

            switch (option) {
                case 1:
                    System.out.println("Create username to signup.");
                    String usernameToSignup = sc.next();
                    sc.nextLine();
                    System.out.println("Create to password to complete signup.");
                    String passwordToSignup = sc.next();
                    User userToSignup = new User(usernameToSignup, passwordToSignup, UserServiceUtil.hashedPassword(passwordToSignup), new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingService.signUpUser(userToSignup);
                    break;

                case 2:
                    System.out.println("Enter username to continue.");
                    String usernameToLogin = sc.next();
                    System.out.println("Enter password to login");
                    String passwordToLogin = sc.next();
                    User userToLogin = new User(usernameToLogin, passwordToLogin, UserServiceUtil.hashedPassword(passwordToLogin), new ArrayList<>(), UUID.randomUUID().toString());

                    try {
                        userBookingService = new UserBookingService(userToLogin);
                    } catch (IOException ex) {
                        return;
                    }
                    break;
                case 3:
                    System.out.println("Fetching your bookings...");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Type your source station.");
                    String source = sc.next();
                    System.out.println("Type your destination station.");
                    String destination = sc.next();

                    List<Train> trains = userBookingService.getTrains(source, destination);

                    int index = 1;

                    for (Train t : trains) {
                        System.out.println(index + " Train ID: " + t.getTrainId());
                        for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                            System.out.println("Station " + entry.getKey() + " time: " + entry.getValue());
                        }
                    }

                    System.out.println("Select a train by typing 1,2,3...");
                    trainSelectedForBooking = trains.get((sc.nextInt()));
                    break;

                case 5:
                    System.out.println("Select a seat out of these seats.");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for(List<Integer> row: seats){
                        for(Integer val:row){
                            System.out.println(val+" ");
                        }
                        System.out.println();
                    }
                    System.out.println("Select the seat by typing the row and column.");
                    System.out.println("Select the row:");
                    int row = sc.nextInt();
                    System.out.println("Select column:");
                    int col = sc.nextInt();

                    System.out.println("Booking your seat...");

                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking,row,col);

                    break;
                case 6:

                // Display user's current bookings first to help them choose
                System.out.println("Your current bookings:");
                userBookingService.fetchBooking(); // Assumes this prints tickets if any
                if (userBookingService.getUser().getTickets() == null || userBookingService.getUser().getTickets().isEmpty()) {
                    // Message already printed by fetchBooking if list is empty
                    break;
                }


                System.out.print("Enter the Ticket ID to cancel: ");
                String ticketIdToCancel = sc.nextLine();

                if (ticketIdToCancel.trim().isEmpty()) {
                    System.out.println("Ticket ID cannot be empty. Cancellation aborted.");
                    break;
                }

                System.out.println("Attempting to cancel booking with Ticket ID: " + ticketIdToCancel + "...");
                Boolean cancelled = userBookingService.cancelBooking(ticketIdToCancel);

                if (cancelled) {
                    // Success message printed within cancelBooking
                } else {
                    // Failure message printed within cancelBooking
                    System.out.println("Cancellation failed.");
                }
                break;

                case 7: // Exit
                    System.out.println("Exiting Train Booking System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please choose between 1 and 7.");
                    break;
                    


            }
        }

    }
}
