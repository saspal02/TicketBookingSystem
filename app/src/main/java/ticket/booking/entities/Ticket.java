package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private Date dateOfTravel;
    private Train train;
    private List<List<Integer>> seatsBooked;

    public Ticket(String ticketId, String userId, String source, String destination, Date dateOfTravel, Train train,List<List<Integer>> seatsBooked) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
        this.seatsBooked = seatsBooked;
    }

    public Ticket() {}

    // Methods
    public String getTicketInfo() {
        String info = String.format(
                "Ticket Information:\n" +
                        "-----------------------------\n" +
                        "Ticket ID      : %s\n" +
                        "User ID        : %s\n" +
                        "Source         : %s\n" +
                        "Destination    : %s\n" +
                        "Date of Travel : %s\n" +
                        "Train Name     : %s\n" +
                        "Seats booked   : %s",
                ticketId, userId, source, destination, dateOfTravel, train.getTrainName(), seatsBooked
        );
        return info;
    }

    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(Date dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
}
