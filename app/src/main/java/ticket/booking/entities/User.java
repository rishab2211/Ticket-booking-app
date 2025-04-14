package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class User {
    private String name;
    // You can keep password or ignore it if not needed for loading
    private String password;

    @JsonProperty("hashed_password")
    private String hashedPassword;

    @JsonProperty("tickets_booked")
    private List<Ticket> tickets;

    // Add annotation for user_id mismatch
    @JsonProperty("user_id")
    private String userId;

    // Constructors (keep existing ones)
    public User(String name, String password, String hashedPassword, List<Ticket> tickets, String userId) {
        this.name = name;
        this.password = password; // Keep for login check? Or remove after hashing?
        this.hashedPassword = hashedPassword;
        this.tickets = tickets != null ? tickets : new ArrayList<>(); // Ensure list is initialized
        this.userId = userId;
    }

    public User() {
        this.tickets = new ArrayList<>();
    }



    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    // Method to print tickets
    public void printTickets() {
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        System.out.println("Your Bookings:");
        for (Ticket ticket : tickets) {
            System.out.println(ticket.getTicketInfo());
        }
    }

    // methods to manage tickets
    public boolean addTicket(Ticket ticket) {
        if (this.tickets == null) {
            this.tickets = new ArrayList<>();
        }
        return this.tickets.add(ticket);
    }

    public Optional<Ticket> findTicketById(String ticketId) {
        if (this.tickets == null) {
            return Optional.empty();
        }
        return this.tickets.stream()
                .filter(ticket -> ticket.getTicketId().equals(ticketId))
                .findFirst();
    }

    public boolean removeTicket(Ticket ticketToRemove) {
        if (this.tickets == null) {
            return false;
        }
        return this.tickets.remove(ticketToRemove);
    }
}