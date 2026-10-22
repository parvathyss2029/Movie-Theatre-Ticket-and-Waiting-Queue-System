import java.util.Scanner;

class Movie {
    int id;
    String name;
    String showTime;
    double ticketPrice;
    int totalSeats;
    int bookedSeats;

    String[] seatNumbers;
    String[] bookedCustomers;
    boolean[] bookingActive;

    String[] waitingQueue;
    int front, rear, waitingCount;

    Movie(int id, String name, String showTime,
          double ticketPrice, int totalSeats) {
        this.id = id;
        this.name = name;
        this.showTime = showTime;
        this.ticketPrice = ticketPrice;
        this.totalSeats = totalSeats;
        bookedSeats = 0;

        seatNumbers = new String[totalSeats];
        bookedCustomers = new String[totalSeats];
        bookingActive = new boolean[totalSeats];

        for (int i = 0; i < totalSeats; i++) {
            seatNumbers[i] = "S" + (i + 1);
            bookingActive[i] = false;
        }

        waitingQueue = new String[100];
        front = 0;
        rear = -1;
        waitingCount = 0;
    }

    int availableSeats() {
        return totalSeats - bookedSeats;
    }

    void display() {
        System.out.println("Movie ID: " + id);
        System.out.println("Movie Name: " + name);
        System.out.println("Show Time: " + showTime);
        System.out.println("Ticket Price: Rs. " + ticketPrice);
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Available Seats: " + availableSeats());
        System.out.println("-----------------------------");
    }

    boolean isCustomerBooked(String customer) {
        for (int i = 0; i < totalSeats; i++) {
            if (bookingActive[i] &&
                bookedCustomers[i].equalsIgnoreCase(customer)) {
                return true;
            }
        }
        return false;
    }

    boolean isCustomerWaiting(String customer) {
        for (int i = 0; i < waitingCount; i++) {
            int index = (front + i) % waitingQueue.length;

            if (waitingQueue[index].equalsIgnoreCase(customer)) {
                return true;
            }
        }
        return false;
    }

    boolean addToWaitingQueue(String customer) {
        if (waitingCount == waitingQueue.length) {
            return false;
        }

        rear = (rear + 1) % waitingQueue.length;
        waitingQueue[rear] = customer;
        waitingCount++;
        return true;
    }

    String removeFromWaitingQueue() {
        if (waitingCount == 0) {
            return null;
        }

        String customer = waitingQueue[front];
        waitingQueue[front] = null;
        front = (front + 1) % waitingQueue.length;
        waitingCount--;

        if (waitingCount == 0) {
            front = 0;
            rear = -1;
        }

        return customer;
    }

    void bookTicket(String customer) {
        if (isCustomerBooked(customer) ||
            isCustomerWaiting(customer)) {
            System.out.println("Customer already booked or waiting.");
            return;
        }

        for (int i = 0; i < totalSeats; i++) {
            if (!bookingActive[i]) {
                bookingActive[i] = true;
                bookedCustomers[i] = customer;
                bookedSeats++;

                System.out.println("Ticket booked successfully!");
                System.out.println("Customer: " + customer);
                System.out.println("Seat Number: " + seatNumbers[i]);
                System.out.println("Amount: Rs. " + ticketPrice);
                return;
            }
        }

        if (addToWaitingQueue(customer)) {
            System.out.println("All seats are booked.");
            System.out.println(customer + " added to waiting queue.");
            System.out.println("Queue position: " + waitingCount);
        } else {
            System.out.println("Waiting queue is full.");
        }
    }

    void cancelTicket(String customer) {
        for (int i = 0; i < totalSeats; i++) {
            if (bookingActive[i] &&
                bookedCustomers[i].equalsIgnoreCase(customer)) {

                bookingActive[i] = false;
                bookedCustomers[i] = null;
                bookedSeats--;

                System.out.println("Booking cancelled for " + customer);

                String nextCustomer = removeFromWaitingQueue();

                if (nextCustomer != null) {
                    bookingActive[i] = true;
                    bookedCustomers[i] = nextCustomer;
                    bookedSeats++;

                    System.out.println("Seat assigned to waiting customer:");
                    System.out.println("Customer: " + nextCustomer);
                    System.out.println("Seat Number: " + seatNumbers[i]);
                }
                return;
            }
        }

        System.out.println("No booking found for " + customer);
    }

    void displayBookings() {
        boolean found = false;

        System.out.println("Bookings for " + name + ":");

        for (int i = 0; i < totalSeats; i++) {
            if (bookingActive[i]) {
                System.out.println(seatNumbers[i] + " - "
                                   + bookedCustomers[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No tickets booked.");
        }
    }

    void displayWaitingQueue() {
        if (waitingCount == 0) {
            System.out.println("Waiting queue is empty.");
            return;
        }

        System.out.println("Waiting Queue (FIFO):");

        for (int i = 0; i < waitingCount; i++) {
            int index = (front + i) % waitingQueue.length;
            System.out.println((i + 1) + ". " + waitingQueue[index]);
        }
    }
}

public class MovieTheatreTicketSystem {
    static Scanner sc = new Scanner(System.in);
    static Movie[] movies = new Movie[10];
    static int movieCount = 0;

    public static void main(String[] args) {
        movies[movieCount++] =
            new Movie(1, "The Adventure", "10:00 AM", 150, 3);

        movies[movieCount++] =
            new Movie(2, "Space Journey", "2:00 PM", 180, 3);

        int choice;

        do {
            System.out.println("\n===== MOVIE THEATRE SYSTEM =====");
            System.out.println("1. Add Movie");
            System.out.println("2. Display Movies");
            System.out.println("3. Check Seat Availability");
            System.out.println("4. Book Ticket");
            System.out.println("5. Cancel Ticket");
            System.out.println("6. Display Bookings");
            System.out.println("7. Display Waiting Queue");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    displayMovies();
                    break;
                case 3:
                    checkAvailability();
                    break;
                case 4:
                    bookTicket();
                    break;
                case 5:
                    cancelTicket();
                    break;
                case 6:
                    displayBookings();
                    break;
                case 7:
                    displayQueue();
                    break;
                case 8:
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 8);

        sc.close();
    }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }

        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    static double readDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a valid price: ");
            sc.next();
        }

        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }

    static Movie findMovie(int id) {
        for (int i = 0; i < movieCount; i++) {
            if (movies[i].id == id) {
                return movies[i];
            }
        }
        return null;
    }

    static Movie selectMovie() {
        displayMovies();

        if (movieCount == 0) {
            return null;
        }

        System.out.print("Enter Movie ID: ");
        int id = readInt();

        Movie movie = findMovie(id);

        if (movie == null) {
            System.out.println("Movie not found.");
        }

        return movie;
    }

    static void addMovie() {
        if (movieCount == movies.length) {
            System.out.println("Movie list is full.");
            return;
        }

        System.out.print("Enter Movie ID: ");
        int id = readInt();

        if (findMovie(id) != null) {
            System.out.println("Movie ID already exists.");
            return;
        }

        System.out.print("Enter Movie Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter Show Time: ");
        String time = sc.nextLine().trim();

        System.out.print("Enter Ticket Price: ");
        double price = readDouble();

        System.out.print("Enter Total Seats (1-100): ");
        int seats = readInt();

        if (name.isEmpty() || time.isEmpty() ||
            price <= 0 || seats < 1 || seats > 100) {
            System.out.println("Invalid movie details.");
            return;
        }

        movies[movieCount++] =
            new Movie(id, name, time, price, seats);

        System.out.println("Movie added successfully.");
    }

    static void displayMovies() {
        if (movieCount == 0) {
            System.out.println("No movies available.");
            return;
        }

        for (int i = 0; i < movieCount; i++) {
            movies[i].display();
        }
    }

    static void checkAvailability() {
        Movie movie = selectMovie();

        if (movie != null) {
            System.out.println("Available Seats: "
                               + movie.availableSeats());
            System.out.println("Booked Seats: " + movie.bookedSeats);
        }
    }

    static void bookTicket() {
        Movie movie = selectMovie();

        if (movie == null) {
            return;
        }

        System.out.print("Enter Customer Name: ");
        String customer = sc.nextLine().trim();

        if (customer.isEmpty()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }

        movie.bookTicket(customer);
    }

    static void cancelTicket() {
        Movie movie = selectMovie();

        if (movie == null) {
            return;
        }

        System.out.print("Enter Customer Name: ");
        String customer = sc.nextLine().trim();

        if (customer.isEmpty()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }

        movie.cancelTicket(customer);
    }

    static void displayBookings() {
        Movie movie = selectMovie();

        if (movie != null) {
            movie.displayBookings();
        }
    }

    static void displayQueue() {
        Movie movie = selectMovie();

        if (movie != null) {
            movie.displayWaitingQueue();
        }
    }
}