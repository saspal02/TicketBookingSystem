package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_FILE_PATH = "/home/saswat/Desktop/IRCTC/app/src/main/resources/localdb/users.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();

    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    private void loadUserListFromFile() throws IOException {
        File usersFile = new File(USERS_FILE_PATH);
        userList = objectMapper.readValue(usersFile, new TypeReference<List<User>>() {
        });
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(currentUser -> {
            return currentUser.getName().equals
                    (user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), currentUser.getHashPassword());
        }).findFirst();
        if (foundUser.isPresent()) {
            this.user.setUserId(foundUser.get().getUserId());
            this.user.setTicketsBooked(foundUser.get().getTicketsBooked());
            this.user.setHashPassword(foundUser.get().getHashPassword());
            return true;
        }

        return false;

    }

    public Boolean signUp(User currentUser) {
        try {
            userList.add(currentUser);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;

        }
    }

    private void saveUserListToFile() throws IOException {
        try {
            objectMapper.writeValue(new File(USERS_FILE_PATH), userList);
        } catch (IOException ex) {
            ex.printStackTrace();

        }

    }

    public void fetchBookings() {
        Optional<User> userFetched = userList.stream().filter(currentUser -> {
            return currentUser.getName().equals
                    (user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), currentUser.getHashPassword());
        }).findFirst();
        if (userFetched.isPresent()) {
            userFetched.get().printTickets();
        }
    }

    public Boolean cancelBooking(String ticketId) {
        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be empty");
            return Boolean.FALSE;
        }

        String finalTicketId = ticketId;
        boolean removed = user.getTicketsBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
        if (removed) {
            System.out.println("Ticket with ID " + ticketId + "has been cancelled");
            return Boolean.TRUE;
        } else {
            System.out.println("No tickets found with this ID");
            return Boolean.FALSE;
        }

    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();

    }

    public Boolean bookTrainSeat(Train train, int row, int seat,String source,String destination) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);

                    createTicket(source,destination,train,seats);

                    return true;// Booking successful
                } else {
                    return false; // Seat is already booked
                }
            } else {
                return false; // Invalid row or seat index
            }
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    public Ticket createTicket(String source,String dest,Train train,List<List<Integer>> bookedSeats) throws IOException {
        Ticket newTicket = new Ticket(UUID.randomUUID().toString(),user.getUserId(),source,dest,new Date(),train,bookedSeats);
        user.getTicketsBooked().add(newTicket);

        Optional<User> existingUser = userList.stream()
                .filter(u -> u.getUserId().equals(user.getUserId()))
                .findFirst();
        if (existingUser.isPresent()) {
            int index = userList.indexOf(existingUser.get());
            userList.set(index,user);
            saveUserListToFile();
        }

        return newTicket;

    }






}
