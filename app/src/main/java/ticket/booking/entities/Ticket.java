package ticket.booking.entities;

// --- ADD THESE IMPORTS ---
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// --- END IMPORTS ---

// --- ADD THIS CLASS ANNOTATION ---
@JsonIgnoreProperties(ignoreUnknown = true) // To ignore "ticket_info" etc.
// --- END CLASS ANNOTATION ---
public class Ticket {

    // --- ADD ANNOTATIONS TO FIELDS ---
    @JsonProperty("ticket_id")
    private String ticketId;

    @JsonProperty("user_id")
    private String userId;

    // No annotation needed if JSON field name ("source") matches Java field name ("source")
    private String source;

    // No annotation needed if JSON field name ("destination") matches Java field name ("destination")
    private String destination;

    @JsonProperty("date_of_travel")
    private String dateOfTravel;

    // No annotation needed if JSON field name ("train") matches Java field name ("train")
    private Train train;

    // These fields (row, seat) are not in your example ticket JSON,
    // so no @JsonProperty needed unless they exist in JSON with different names.
    // They will be initialized to 0 when loading from JSON.
    private int row;
    private int seat;
    // --- END ANNOTATIONS ---

    // constructor with values (keep this for creating new tickets in your code)
    public Ticket(String ticketId, String userId, String source, String destination, String dateOfTravel, Train train, int row, int seat) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
        this.row = row;
        this.seat = seat;
    }

    // default constructor without any value (keep this, Jackson often needs it)
    public Ticket() {
    }


    // Getters (Keep all getters)
    public String getTicketId() {
        return ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public Train getTrain() {
        return train;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }


    // getTicketInfo method (Keep this)
    public String getTicketInfo() {
        // Ensure train is not null before calling methods on it
        String trainIdentifier = (train != null) ? train.getTrainId() : "N/A";
        return String.format("Ticket ID: %s belongs to User %s from %s to %s on %s (Train: %s, Seat: R%d C%d)",
                ticketId, userId, source, destination, dateOfTravel, trainIdentifier, row, seat);
    }

    // Setters (Keep all setters - Jackson uses these)
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }
}