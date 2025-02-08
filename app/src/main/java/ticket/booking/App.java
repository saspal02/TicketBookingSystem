package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;

        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            System.out.println("There is something wrong");
            ex.printStackTrace();
            return;
        }
        //Global variables
        Train trainSelectedForBooking = new Train();
        String src = null;
        String dest = null;

        while (option!=7) {
            System.out.println("Choose Option");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search trains");
            System.out.println("5. Book a seat");
            System.out.println("6. CancelBooking");
            System.out.println("7. Exit the app");
            option =scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();
                    User userToSignUp= new User(nameToSignUp,passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp),new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingService.signUp(userToSignUp);

                    break;
                case 2:
                    System.out.println("Enter the username to Login");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to Login");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(nameToLogin, passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(),"");

                    try {
                        userBookingService = new UserBookingService(userToLogin);
                        if (userBookingService.loginUser()) {
                            System.out.println("Login successfull!");

                        } else {
                            System.out.println("Login unsuccessful");

                        }

                    } catch (IOException ex) {
                        System.out.println("Error logging in.");
                        ex.printStackTrace();
                        return;
                    }
                    break;

                case 3:
                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBookings();
                    break;
                case 4:
                    System.out.println("Type your Source station");
                    src = scanner.next();
                    System.out.println("Type your source Destination");
                    dest = scanner.next();

                    if (src.isEmpty() || dest.isEmpty()) {
                        System.out.println("Source or Destination cannot be empty");
                        break;
                    }

                    List<Train> trains = userBookingService.getTrains(src,dest);
                    if (trains.isEmpty()) {
                        System.out.println("No trains found for provided source and destination");
                        break;
                    }
                    int index = 1; // Start from 1 for the first train

                    for (Train t : trains) {
                        System.out.println("-------------------------------------------------");
                        System.out.println("Train #" + index);
                        System.out.println("Train ID: " + t.getTrainId());
                        System.out.println("Train Name: " + t.getTrainName()); // Added train name

                        System.out.println("Station Times:");
                        for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                            System.out.println("Station: " + entry.getKey() + " | Time: " + entry.getValue());
                        }
                        System.out.println("-------------------------------------------------");
                        index++;
                    }
                    System.out.println("Select a train by typing 1,2,3..");
                    int trainIndex = scanner.nextInt();
                    if (trainIndex < 1 || trainIndex > trains.size()) {
                        System.out.println("Invalid selection. Please select a valid train number");
                        break;
                    }
                    trainSelectedForBooking = trains.get(trainIndex - 1);
                    System.out.println("You have selected train #" + trainIndex + " - " + trainSelectedForBooking.getTrainName());
                    break;

                case 5:
                    System.out.println("Select a seat out of these seats");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for (List<Integer> row: seats) {
                        for (Integer val: row) {
                            System.out.print(val + " ");
                        }
                        System.out.println();

                    }

                    System.out.println("Select the seat by typing the row and column");
                    System.out.println("Enter the row");
                    int row = scanner.nextInt();
                    System.out.println("Enter the column");
                    int col = scanner.nextInt();

                    System.out.println("Booking your seat....");
                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking,row,col,src,dest);

                    if (booked.equals(Boolean.TRUE )) {
                        System.out.println("Booked! Enjoy your Journey");


                    } else {

                        System.out.println("Cant book this seat");
                    }
                    break;

                case 6:
                    System.out.println("Enter the ticket id to cancel");
                    String ticketId = scanner.next();
                    if (userBookingService.cancelBooking(ticketId)) {
                        System.out.println("Ticket successfully cancelled");
                    } else {
                        System.out.println("Failed to cancel ticker.Please check the ticket it ");
                    }
                    break;


                default:
                    System.out.println("Invalid Option,please try again");
            }



        }


    }
}
