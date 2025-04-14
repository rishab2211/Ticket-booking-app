package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;

    private List<User> userList;

    private static final String USERS_PATH = "app/src/main/resources/users.json";

    private ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUsers();
    }

    public UserBookingService() throws IOException {
        loadUsers();
    }

//    public List<User> loadUsers() throws IOException {
//        File users = new File(USERS_PATH);
//        return OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {
//        });
//    }

    public List<User> loadUsers() throws IOException {
        // Load resource from classpath instead of direct file path
        userList = OBJECT_MAPPER.readValue(
                getClass().getClassLoader().getResourceAsStream("users.json"),
                new TypeReference<List<User>>() {}
        );
        return userList;
    }

    public Boolean loginUser(User user) {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getPassword());
        }).findFirst();

        return foundUser.isPresent();
    }

    public Boolean signUpUser(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

//    private void saveUserListToFile() throws IOException {
//        File usersFile = new File(USERS_PATH);
//        OBJECT_MAPPER.writeValue(usersFile, userList);
//    }

    private void saveUserListToFile() throws IOException {
        // Get the path to the users.json file
        String userFilePath = getClass().getClassLoader().getResource("users.json").getFile();
        File usersFile = new File(userFilePath);
        OBJECT_MAPPER.writeValue(usersFile, userList);
    }
    // json --> Object(User) --> Deserialize
    // Object(User) --> Json --> Serialize

    public void fetchBooking() {
        user.printTickets();
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex) {
            return null;

        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();

            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true; // booking successful
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    // Updated cancelBooking method
    public Boolean cancelBooking(String ticketId) {
        if (this.user == null) {
            System.out.println("Error: No user logged in.");
            return Boolean.FALSE;
        }

        // Find the ticket in the current user's list
        Optional<Ticket> ticketToCancelOpt = user.findTicketById(ticketId);

        if (!ticketToCancelOpt.isPresent()) {
            System.out.println("Error: Ticket ID " + ticketId + " not found in your bookings.");
            return Boolean.FALSE;
        }

        Ticket ticketToCancel = ticketToCancelOpt.get();
        Train train = ticketToCancel.getTrain();
        int row = ticketToCancel.getRow();
        int seat = ticketToCancel.getSeat();

        try {
            // Mark the seat as available again on the Train object
            List<List<Integer>> seats = train.getSeats();
            // validation
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 1) { // Check if it was actually booked
                    seats.get(row).set(seat, 0); // Set back to 0 (available)
                    train.setSeats(seats);
                } else {
                    System.out.println("Warning: Seat R" + row + " C" + seat + " on Train " + train.getTrainId() + " was already marked as available.");
                }
            } else {
                System.out.println("Error: Invalid seat details (R" + row + " C" + seat + ") found in ticket " + ticketId + ". Cannot update train status.");
                // Decide if cancellation should still proceed. Let's stop here to be safe.
                return Boolean.FALSE;
            }


            // Update the train data in the storage
            TrainService trainService = new TrainService();
            // Assuming addTrain updates the train if it exists based on trainId
            boolean trainUpdated = trainService.addTrain(train);
            if (!trainUpdated) {
                System.out.println("Error: Could not update train information in storage.");
                return Boolean.FALSE;
            }

            // Remove the ticket from the user's list
            boolean removed = user.removeTicket(ticketToCancel);
            if (!removed) {

                System.out.println("Error: Could not remove ticket from user's list.");
                return Boolean.FALSE;
            }


            // 5. Update the user's data in persistent storage (users.json)

            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getUserId().equals(user.getUserId())) {
                    userList.set(i, user); // Replace the user in the list with the updated one
                    break;
                }
            }
            saveUserListToFile(); // Save the entire updated list

            System.out.println("Booking with Ticket ID " + ticketId + " cancelled successfully.");
            return Boolean.TRUE;

        } catch (IOException e) {
            System.err.println("Error during cancellation process: " + e.getMessage());

            return Boolean.FALSE;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during cancellation: " + e.getMessage());
            e.printStackTrace(); // stack trace for debugging
            return Boolean.FALSE;
        }
    }

    // Helper method to get the currently logged-in user
    public User getUser() {
        return this.user;
    }
}